package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.ui.view.activity.ProductDetail;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.Models.Product;  // Đảm bảo import đúng package
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;

    public void Update(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }
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
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()) + "/" + product.getProduct_type().getName());
        holder.productRating.setText(String.valueOf(product.getAverage_rating()));

        // Lấy imageUrl từ Product (ảnh chính)
        String imageUrl = product.getImageUrl();
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(holder.productImage);  // Hiển thị ảnh vào ImageView
        }

        // Thiết lập sự kiện click cho mỗi item sản phẩm
        holder.itemView.setOnClickListener(v -> {
            // Chuyển sang Activity chi tiết sản phẩm
            Intent intent = new Intent(context, ProductDetail.class);
            // Truyền thông tin cơ bản sản phẩm qua Intent
            intent.putExtra("product_id", product.get_id());  // Chuyển ID sản phẩm
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", String.valueOf(product.getPrice()));
            intent.putExtra("product_rating", String.valueOf(product.getAverage_rating()));
            intent.putExtra("product_image_url", product.getImageUrl());  // Thêm URL ảnh
            context.startActivity(intent);  // Mở ProductDetailActivity
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
