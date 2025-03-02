package com.md09.pharmapoly.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    private List<Integer> imageIds;  // Thay vì list URL, giờ là list các resource ID drawable

    public SliderAdapter(List<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Thay vì tải ảnh từ URL, sử dụng resource ID từ drawable
        holder.imageView.setImageResource(imageIds.get(position));
    }

    @Override
    public int getItemCount() {
        return imageIds.size();  // Số lượng hình ảnh
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);  // ID của ImageView trong item_slider.xml
        }
    }
}

