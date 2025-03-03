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
import com.md09.pharmapoly.Models.Product;
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

        // Set product name, ensure it's not null
        holder.productName.setText(product.getName() != null ? product.getName() : "N/A");

        // Check if ProductType is not null before getting its name
        if (product.getProduct_type() != null) {
            holder.productPrice.setText(product.getPrice() + "/" + product.getProduct_type().getName());
        } else {
            holder.productPrice.setText(product.getPrice() + "/ N/A");
        }

        // Set product rating
        holder.productRating.setText(String.valueOf(product.getAverage_rating()));

        // Load image if URL is not null
        String imageUrl = product.getImageUrl();
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            // Optional: Set a default image if imageUrl is null
            holder.productImage.setImageResource(R.drawable.group6574);
        }

        // Set click listener to open ProductDetail Activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetail.class);
            intent.putExtra("product_id", product.get_id());
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", String.valueOf(product.getPrice()));
            intent.putExtra("product_rating", String.valueOf(product.getAverage_rating()));
            intent.putExtra("product_image_url", product.getImageUrl());
            context.startActivity(intent);
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
