package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.Product;
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
        holder.productName.setText(product.getName());
        holder.productPrice.setText(""+product.getPrice());
        holder.productRating.setText(""+product.getAverageRating());

        // Lấy imageUrl từ Product
        String imageUrl = product.getImageUrl();

        // Nếu có imageUrl thì tải ảnh
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(holder.productImage);  // Hiển thị ảnh vào ImageView
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating;
        ImageView productImage;  // Đây là ImageView mà bạn cần để hiển thị ảnh

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            productImage = itemView.findViewById(R.id.productImage);  // Ánh xạ đúng ID cho ImageView
        }
    }
}
