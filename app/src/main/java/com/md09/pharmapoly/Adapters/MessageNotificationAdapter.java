package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.NotificationItem;
import com.md09.pharmapoly.R;

import java.util.List;

public class MessageNotificationAdapter extends RecyclerView.Adapter<MessageNotificationAdapter.ViewHolder> {
    private List<NotificationItem> notificationList;

    // Constructor
    public MessageNotificationAdapter(Context context, List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_in_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notification = notificationList.get(position);
        holder.txtTitle.setText(notification.getTitle());
        holder.txtTime.setText(notification.getTime());
        holder.imgIcon.setImageResource(notification.getIcon());

        // Hiển thị chấm nếu thông báo chưa được đọc
        if (notification.isUnread()) {
            holder.unreadDot.setVisibility(View.VISIBLE);
        } else {
            holder.unreadDot.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder để ánh xạ các views trong item
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

