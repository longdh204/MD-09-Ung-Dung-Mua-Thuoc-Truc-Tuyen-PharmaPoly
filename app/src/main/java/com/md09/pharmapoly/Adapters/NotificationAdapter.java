package com.md09.pharmapoly.Adapters;

import static com.md09.pharmapoly.utils.Constants.NOTIFICATION_READ_ALL_KEY;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.md09.pharmapoly.Models.Notification;
import com.md09.pharmapoly.Models.NotificationModel;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notificationList;
    private Context context;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }
    public void Update(List<Notification> notifications) {
        this.notificationList = notifications;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        if (notification != null) {
            holder.txtTitle.setText(notification.getTitle());
            holder.txtContent.setText(notification.getMessage());
            holder.txtTime.setText(formatCreatedAt(notification.getCreated_at()));

            if (notification.isIs_read()) {
                holder.unreadDot.setVisibility(View.GONE);
            } else {
                holder.unreadDot.setVisibility(View.VISIBLE);
            }
            holder.itemView.setOnClickListener(v -> {
                if (notification.isIs_read()) return;
                new SharedPrefHelper(context).setBooleanState(NOTIFICATION_READ_ALL_KEY,true);
                new RetrofitClient()
                        .callAPI()
                        .readNotification(notification.get_id(),"Bearer " + new SharedPrefHelper(context).getToken())
                        .enqueue(new Callback<ApiResponse<Notification>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<Notification>> call, Response<ApiResponse<Notification>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        notification.setIs_read(true);
                                        notifyItemChanged(position);

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<Notification>> call, Throwable t) {

                            }
                        });
            });
        }
    }
    public String formatCreatedAt(Date created_at) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'vào' HH:mm", Locale.getDefault());
        return sdf.format(created_at);
    }
    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtContent, txtTime;
        ImageView imgIcon;
        ImageView unreadDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_notification_title);
            txtContent = itemView.findViewById(R.id.txt_notification_content);
            txtTime = itemView.findViewById(R.id.txt_notification_time);
            imgIcon = itemView.findViewById(R.id.img_notification_icon);
            unreadDot = itemView.findViewById(R.id.unread_dot);
        }
    }
}

