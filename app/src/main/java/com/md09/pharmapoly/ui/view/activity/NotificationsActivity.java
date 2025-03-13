package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.md09.pharmapoly.Adapters.NotificationAdapter;
import com.md09.pharmapoly.Models.NotificationModel;
import com.md09.pharmapoly.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish()); // Đóng activity hiện tại

        notificationList = new ArrayList<>();
        notificationList.add(new NotificationModel("Thông báo 1", "11/12/2004 vào 12:01", R.drawable.ic_bell, true));
        notificationList.add(new NotificationModel("Thông báo 2", "11/12/2004 vào 12:01", R.drawable.ic_bell, false));
        notificationList.add(new NotificationModel("Thông báo 3", "11/12/2004 vào 12:01", R.drawable.ic_bell, false));

        adapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(adapter);
    }
}
