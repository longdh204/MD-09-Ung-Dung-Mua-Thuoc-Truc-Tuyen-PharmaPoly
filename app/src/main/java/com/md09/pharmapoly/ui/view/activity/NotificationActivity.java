package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.MessageNotificationAdapter;
import com.md09.pharmapoly.Adapters.RecyclerItemClickListener;
import com.md09.pharmapoly.Models.NotificationItem;
import com.md09.pharmapoly.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageNotificationAdapter adapter;
    private List<NotificationItem> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerView);
        notificationList = new ArrayList<>();

        // Giả sử bạn đã thêm các thông báo vào notificationList
        adapter = new MessageNotificationAdapter(this, notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Cập nhật lại cách sử dụng RecyclerItemClickListener trong NotificationActivity.java
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Lấy thông tin thông báo từ item được chọn
                NotificationItem notification = notificationList.get(position);

                // Tạo Intent để mở NotificationDetailActivity
                Intent intent = new Intent(NotificationActivity.this, NotificationDetailActivity.class);
                intent.putExtra("title", notification.getTitle());
                intent.putExtra("message", notification.getMessage());
                intent.putExtra("time", notification.getTime());

                // Mở Activity chi tiết thông báo
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Xử lý nếu có hành động khác khi nhấn dài
            }
        }));
    }
}

