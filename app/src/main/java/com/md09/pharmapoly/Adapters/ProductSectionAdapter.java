package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.ProductSection;
import com.md09.pharmapoly.Models.ProductSectionDetail;
import com.md09.pharmapoly.R;

import java.util.List;

public class ProductSectionAdapter extends RecyclerView.Adapter<ProductSectionAdapter.ViewHolder> {
    private Context context;
    private List<ProductSection> sectionList;
    public ProductSectionAdapter(Context context, List<ProductSection> sectionList) {
        this.context = context;
        this.sectionList = sectionList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductSection section = sectionList.get(position);
        holder.sectionTitle.setText(section.getSection().getName());

        StringBuilder sectionContent = new StringBuilder();
        for (ProductSectionDetail detail : section.getDetails()) {
            sectionContent.append(detail.getTitle())
                    .append(": ")
                    .append(detail.getContent())
                    .append("\n\n");
        }
        holder.sectionContent.setText(sectionContent.toString());
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public void updateSections(List<ProductSection> sections) {
        sectionList.clear();
        sectionList.addAll(sections);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle, sectionContent;

        public ViewHolder(View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
            sectionContent = itemView.findViewById(R.id.sectionContent);
        }
    }
}

