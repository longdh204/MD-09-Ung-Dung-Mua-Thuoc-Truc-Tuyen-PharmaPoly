package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.Models.ItemCategoryHome;
import com.md09.pharmapoly.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private Context context;
    private List<ItemCategoryHome> categoryList;
    private OnCategoryClickListener listener;

    public CategoryListAdapter(Context context, List<ItemCategoryHome> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCategoryHome category = categoryList.get(position);
        holder.name.setText(category.getName());
        holder.icon.setImageResource(category.getIconResId()); // Sử dụng getIconResId()

        // Set click listener on the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(ItemCategoryHome category);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtCategoryName);
            icon = itemView.findViewById(R.id.imgCategory);
        }
    }
}

