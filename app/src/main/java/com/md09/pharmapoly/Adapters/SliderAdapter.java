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

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<String> sliderImageUrls; // URL của các hình ảnh

    // Constructor nhận vào danh sách hình ảnh (có thể là URL hoặc hình ảnh cục bộ)
    public SliderAdapter(List<String> sliderImageUrls) {
        this.sliderImageUrls = sliderImageUrls;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        String imageUrl = sliderImageUrls.get(position);

        // Nếu bạn dùng URL
        Picasso.get().load(imageUrl).into(holder.sliderImage);

        // Nếu bạn dùng hình ảnh cục bộ, thay đổi cách tải ảnh (ví dụ: sử dụng Glide hoặc đơn giản là set ảnh từ drawable)
        // holder.sliderImage.setImageResource(R.drawable.image1); // Cách này sử dụng hình ảnh trong drawable
    }

    @Override
    public int getItemCount() {
        return sliderImageUrls.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView sliderImage;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImage = itemView.findViewById(R.id.sliderImage);
        }
    }
}
