package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.md09.pharmapoly.Medication_reminder;
import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.R;

import java.util.List;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtCategoryName.setText(category.getName());
        holder.imgCategory.setImageResource(category.getImageRes());

        // Xử lý sự kiện click ngay trong Adapter
        holder.itemView.setOnClickListener(v -> {
            switch (category.getName()) {
//                case "Cần mua thuốc":
//                    context.startActivity(new Intent(context, Nav_Medicine.class));
//                    break;
                case "Nhắc nhở uống thuốc":
                    context.startActivity(new Intent(context, Medication_reminder.class));
                    break;
//                case "Bảo hiểm":
//                    context.startActivity(new Intent(context, InsuranceActivity.class));
//                    break;
//                case "Thông tin":
//                    context.startActivity(new Intent(context, InfoActivity.class));
//                    break;
//                case "Ghi chú":
//                    context.startActivity(new Intent(context, NotesActivity.class));
//                    break;
//                case "Đơn thuốc":
//                    context.startActivity(new Intent(context, PrescriptionActivity.class));
//                    break;
//                case "Lịch hẹn":
//                    context.startActivity(new Intent(context, AppointmentActivity.class));
//                    break;
                default:
                    Toast.makeText(context, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName;
        ImageView imgCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            imgCategory = itemView.findViewById(R.id.imgCategory);
        }
    }
}
