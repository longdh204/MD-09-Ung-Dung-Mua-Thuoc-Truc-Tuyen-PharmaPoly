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
    private UpdateReminderListener updateReminderListener;

    // Interface để gọi lại phương thức trong Activity khi người dùng nhấn "Cập nhật"
    public interface UpdateReminderListener {
        void onUpdateReminder(Reminder reminder);
    }

    public MedicineReminderAdapter(List<Reminder> reminderList, SharedPrefHelper sharedPrefHelper, UpdateReminderListener listener) {
        this.reminderList = reminderList;
        this.sharedPrefHelper = sharedPrefHelper;
        this.updateReminderListener = listener;
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

        // Kiểm tra lặp lại mỗi ngày
        StringBuilder repeatText = new StringBuilder();
        if (reminder.isRepeatDaily()) {
            repeatText.append("Lặp lại mỗi ngày");
        }

        // Kiểm tra lặp lại mỗi giờ và hiển thị các giờ đã chọn
        if (reminder.isRepeatHourly()) {
            if (repeatText.length() > 0) {
                repeatText.append(" & ");
            }
            repeatText.append("Lặp lại mỗi giờ: ");
            // Đảm bảo hours không phải null trước khi lặp qua
            if (reminder.getHours() != null && !reminder.getHours().isEmpty()) {
                for (int hour : reminder.getHours()) {
                    repeatText.append(String.format("%02d:00 ", hour));  // Thêm giờ vào danh sách
                }
            }
        }

        // Hiển thị thông tin lặp lại trên item
        holder.repeatType.setText(repeatText.toString());

        // Xử lý nút Xóa
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
        Button deleteButton, updateButton;

        public ViewHolder(View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            time = itemView.findViewById(R.id.time);
            repeatType = itemView.findViewById(R.id.repeatType);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            updateButton = itemView.findViewById(R.id.update);  // Gắn nút "Cập nhật"
        }
    }
}
