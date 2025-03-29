package com.md09.pharmapoly.Adapters;

import static com.md09.pharmapoly.utils.Constants.findObjectById;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.md09.pharmapoly.ui.view.activity.ProductDetail;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.Models.Product;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
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
        // Kiểm tra dữ liệu từ API (Logcat để debug)
        Log.d("PRODUCT_DEBUG", "Name: " + product.getName() +
                " | Price: " + product.getPrice() +
                " | Rating: " + product.getAverage_rating());
        // Hiển thị tên sản phẩm
        holder.productName.setText(product.getName() != null ? product.getName() : "Không có tên");

        // Hiển thị giá (Nếu giá <= 0, hiển thị 'Liên hệ')
//        if (product.getPrice() > 0) {
//            holder.productPrice.setText(product.getPrice() + "đ / " +
//                    (product.getProduct_type() != null ? product.getProduct_type().getName() : "N/A"));
//        } else {
//            holder.productPrice.setText("Liên hệ");
//        }
        holder.productPrice.setText(formatCurrency(product.getPrice(),"đ") + "/" +
                (product.getProduct_type() != null ? product.getProduct_type().getName() : "N/A"));

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
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice() >0 ? String.valueOf(product.getPrice()) : "Liên hệ");
            intent.putExtra("product_rating", product.getAverage_rating() >0 ? String.valueOf(product.getAverage_rating()) : "-");
            intent.putExtra("product_image_url", product.getImageUrl());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    public void Update(List<Product> products) {
        this.productList.clear();
        this.productList.addAll(products);
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating;
        ImageView productImage;
        RelativeLayout layout_sale;
        LinearLayout layout_rating;
        public ViewHolder(View itemView) {
            super(itemView);
            layout_sale = itemView.findViewById(R.id.layout_sale);
            layout_rating = itemView.findViewById(R.id.layout_rating);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
