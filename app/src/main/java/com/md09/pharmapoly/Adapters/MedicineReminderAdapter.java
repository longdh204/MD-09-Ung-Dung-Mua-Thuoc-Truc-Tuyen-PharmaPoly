package com.md09.pharmapoly.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Reminder;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.List;

public class MedicineReminderAdapter extends RecyclerView.Adapter<MedicineReminderAdapter.ViewHolder> {

    private List<Reminder> reminderList;
    private SharedPrefHelper sharedPrefHelper;

    public MedicineReminderAdapter(List<Reminder> reminderList, SharedPrefHelper sharedPrefHelper) {
        this.reminderList = reminderList;
        this.sharedPrefHelper = sharedPrefHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.medicineName.setText(reminder.getMedicineName());
        String time = String.format("%02d:%02d", reminder.getHour(), reminder.getMinute());
        holder.time.setText(time);

        // Xử lý màu sắc dựa trên kiểu lặp lại
        if (reminder.isRepeatDaily()) {
            holder.time.setTextColor(Color.GREEN);  // Màu xanh cho lặp lại hàng ngày
            holder.repeatType.setText("Lặp lại mỗi ngày");
        } else {
            holder.time.setTextColor(Color.BLUE);  // Màu xanh dương cho lặp lại theo giờ
            holder.repeatType.setText("Lặp lại mỗi giờ");
        }

        holder.deleteButton.setOnClickListener(v -> {
            sharedPrefHelper.deleteReminder(position);
            reminderList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, time, repeatType;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            time = itemView.findViewById(R.id.time);
            repeatType = itemView.findViewById(R.id.repeatType);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}