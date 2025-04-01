package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Reminder;  // Đảm bảo Reminder được import
import com.md09.pharmapoly.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Reminder> reminderList;  // Sử dụng Reminder thay vì NotificationModel
    private Context context;

    public NotificationAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);

        holder.txtTitle.setText(reminder.getMedicineName());  // Hiển thị tên thuốc hoặc thông báo
        holder.txtTime.setText(reminder.getHour() + ":" + reminder.getMinute());  // Hiển thị giờ

        // Chỉnh sửa nếu có icon hoặc thông tin khác
        holder.imgIcon.setImageResource(R.drawable.ic_bell); // Hoặc thay đổi biểu tượng nếu cần

        // Nếu bạn muốn thêm logic về chưa đọc hay đã đọc, có thể sử dụng thuộc tính nào đó từ Reminder
        holder.unreadDot.setVisibility(View.VISIBLE);  // Điều chỉnh lại logic hiển thị dấu chấm đỏ
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtTime;
        ImageView imgIcon;
        View unreadDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_notification_title);
            txtTime = itemView.findViewById(R.id.txt_notification_time);
            imgIcon = itemView.findViewById(R.id.img_notification_icon);
            unreadDot = itemView.findViewById(R.id.unread_dot);
        }
    }
}
