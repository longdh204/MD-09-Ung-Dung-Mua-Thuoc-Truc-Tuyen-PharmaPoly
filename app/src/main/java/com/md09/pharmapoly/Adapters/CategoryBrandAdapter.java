package com.md09.pharmapoly.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.R;

import java.util.List;

public class CategoryBrandAdapter extends RecyclerView.Adapter<CategoryBrandAdapter.ViewHolder> {

    private List<String> categories;
    private List<String> brands;

    public CategoryBrandAdapter(List<String> categories, List<String> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_brand, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < categories.size()) {
            holder.categoryTextView.setText("Category: " + categories.get(position));
        } else {
            holder.brandTextView.setText("Brand: " + brands.get(position - categories.size()));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size() + brands.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        TextView brandTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            brandTextView = itemView.findViewById(R.id.brandTextView);
        }
    }
}
