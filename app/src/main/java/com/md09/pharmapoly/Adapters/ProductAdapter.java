package com.md09.pharmapoly.Adapters;

import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.ProductProductType;
import com.md09.pharmapoly.ui.view.activity.ProductDetail;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.Models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private int maxHeight = 0;
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.layout_sale.setVisibility(View.GONE);

        holder.productName.setText(product.getName() != null ? product.getName() : "Không có tên");
        holder.tv_specification.setText(product.getSpecification());
        List<View> typeViews = new ArrayList<>();


        holder.itemView.post(() -> {
            if (product.getProduct_types().size() > 1) {

                int itemCount = product.getProduct_types().size();
                int parentWidth = holder.itemView.getWidth() - 72;
                int columns = (itemCount == 2) ? 2 : 3;
                int itemWidth = (parentWidth / columns);
                holder.layout_product_type.removeAllViews();
                for (int i = 0; i < product.getProduct_types().size(); i++) {
                    final int index = i;
                    ProductProductType item = product.getProduct_types().get(i);
//                }
//                for (ProductProductType item : product.getProduct_types()) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_product_type_full, null, false);
                    CardView product_type = view.findViewById(R.id.product_type);
                    TextView tv_type_name = view.findViewById(R.id.tv_type_name);
                    tv_type_name.setText(item.getName());

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.setLayoutParams(params);

                    typeViews.add(view);

                    view.setOnClickListener(v -> {
                        product.setSelectedTypeIndex(index);
                        for (View vType : typeViews) {
                            CardView card = vType.findViewById(R.id.product_type);
                            TextView tv = vType.findViewById(R.id.tv_type_name);
                            card.setCardBackgroundColor(context.getColor(R.color.gray_6F4));
                            tv.setTextColor(context.getColor(R.color.black_333));
                        }


                        product_type.setCardBackgroundColor(context.getColor(R.color.blue_CE4));
                        tv_type_name.setTextColor(context.getColor(R.color.white_FFF));
                        holder.productPrice.setText(
                                formatCurrency(item.getPrice(), "đ") +
                                        "/" +
                                        item.getName());
                    });
                    holder.layout_product_type.addView(view);
                }
                if (!typeViews.isEmpty()) {
                    typeViews.get(0).performClick();
                }
            } else {
                holder.layout_product_type.setVisibility(View.GONE);
                holder.productPrice.setText(
                        formatCurrency(product.getProduct_types().get(0).getPrice(), "đ") +
                                "/" +
                                product.getProduct_types().get(0).getName());
            }
        });

        // Hiển thị đánh giá (Nếu 0.0 thì không hiển thị)
        if (product.getAverage_rating() > 0) {
            holder.productRating.setText(String.valueOf(product.getAverage_rating()));
        } else {
            holder.layout_rating.setVisibility(View.GONE);
            holder.productRating.setText("-"); // Thay vì "-"
        }
        // Load hình ảnh sản phẩm
        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.group6574);
        }
        // Set click listener to open ProductDetail Activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetail.class);
            intent.putExtra("product_id", product.get_id());
            intent.putExtra("selectedTypeIndex", product.getSelectedTypeIndex());
            //intent.putExtra("product_name", product.getName());
//            intent.putExtra("product_price", product.getPrice() > 0 ? String.valueOf(product.getPrice()) : "Liên hệ");
            //intent.putExtra("product_rating", product.getAverage_rating() > 0 ? String.valueOf(product.getAverage_rating()) : "-");
            //intent.putExtra("product_image_url", product.getImageUrl());
            context.startActivity(intent);
        });

        holder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int height = holder.itemView.getHeight();
                if (height > maxHeight) {
                    maxHeight = height;
                    notifyDataSetChanged();
                }

                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.height = maxHeight;
                holder.itemView.setLayoutParams(params);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public void Update(List<Product> products) {
        this.productList.clear();
        this.productList.addAll(products);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating, tv_specification;
        ImageView productImage;
        RelativeLayout layout_sale;
        LinearLayout layout_rating, layout_product_type;

        public ViewHolder(View itemView) {
            super(itemView);
            layout_sale = itemView.findViewById(R.id.layout_sale);
            layout_rating = itemView.findViewById(R.id.layout_rating);
            layout_product_type = itemView.findViewById(R.id.layout_product_type);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            productImage = itemView.findViewById(R.id.productImage);
            tv_specification = itemView.findViewById(R.id.tv_specification);
        }
    }
}
