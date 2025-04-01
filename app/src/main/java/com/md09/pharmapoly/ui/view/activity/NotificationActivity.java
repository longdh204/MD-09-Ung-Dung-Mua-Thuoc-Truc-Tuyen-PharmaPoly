package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.NotificationAdapter;
import com.md09.pharmapoly.Adapters.RecyclerItemClickListener;
import com.md09.pharmapoly.Models.Reminder;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Reminder> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerView);
        reminderList = new ArrayList<>();

        // Đảm bảo rằng bạn đã thêm các thông báo vào reminderList
        // Gán Adapter cho RecyclerView
        notificationAdapter = new NotificationAdapter(this, reminderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notificationAdapter);

        // Cập nhật lại cách sử dụng RecyclerItemClickListener trong NotificationActivity.java
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Lấy thông tin thông báo từ item được chọn
                Reminder reminder = reminderList.get(position);

                // Tạo Intent để mở NotificationDetailActivity
                Intent intent = new Intent(NotificationActivity.this, NotificationDetailActivity.class);
                intent.putExtra("title", reminder.getMedicineName());
                intent.putExtra("time", reminder.getHour() + ":" + reminder.getMinute());

                // Mở Activity chi tiết thông báo
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Xử lý nếu có hành động khác khi nhấn dài
            }
        }));

        // Gọi SharedPrefHelper để lấy và lưu dữ liệu
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);

        // Dữ liệu nhắc nhở (ví dụ)
        String medicineName = "Paracetamol";
        int hour = 8;
        int minute = 30;
        boolean repeatDaily = true;
        boolean repeatHourly = false;
        List<Integer> selectedHours = new ArrayList<>();

        // Lưu vào SharedPreferences
        sharedPrefHelper.saveReminder(medicineName, hour, minute, repeatDaily, repeatHourly, selectedHours);

        // Log dữ liệu đã lưu
        Log.d("SharedPref", "Đã lưu nhắc nhở: " + medicineName + " at " + hour + ":" + minute);

        // Lấy danh sách reminder từ SharedPreferences
        reminderList = sharedPrefHelper.getReminders();

        // Log kiểm tra kích thước và nội dung reminderList
        Log.d("SharedPref", "Retrieved reminder list size: " + reminderList.size());

        // Kiểm tra nếu có dữ liệu
        if (reminderList != null && !reminderList.isEmpty()) {
            // Gán adapter cho RecyclerView nếu có dữ liệu
            notificationAdapter = new NotificationAdapter(this, reminderList);
            recyclerView.setAdapter(notificationAdapter);
        } else {
            // Nếu không có dữ liệu, hiển thị thông báo
            Log.e("RecyclerView", "Dữ liệu không có sẵn!");
        }
    }
}
