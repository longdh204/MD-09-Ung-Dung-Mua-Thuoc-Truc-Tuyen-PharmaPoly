package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.NotificationAdapter;
import com.md09.pharmapoly.Models.NotificationItem;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        // Kiểm tra nếu dữ liệu có sẵn và gán adapter
//        if (notificationList != null && !notificationList.isEmpty()) {
//            notificationAdapter = new NotificationAdapter(this, notificationList);
//            recyclerView.setAdapter(notificationAdapter);  // Gán adapter sau khi có dữ liệu
//        } else {
//            Log.e("NotificationActivity", "No notifications found!");
//        }
    }

}

