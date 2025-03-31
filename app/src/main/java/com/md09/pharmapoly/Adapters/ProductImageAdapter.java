package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder> {
    private List<String> imageList;  // Danh sách các URL hoặc ID của hình ảnh
    private Context context;

    public ProductImageAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String imageUrl = imageList.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView);  // Sử dụng Picasso để tải ảnh
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void updateImages(List<String> newImages) {
        this.imageList = newImages;
        notifyDataSetChanged();  // Cập nhật adapter với dữ liệu mới
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }
}
