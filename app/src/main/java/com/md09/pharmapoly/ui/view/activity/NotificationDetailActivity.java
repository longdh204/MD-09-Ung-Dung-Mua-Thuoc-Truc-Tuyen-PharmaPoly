package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.R;

public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        // Lấy thông tin thông báo từ Intent
        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");
        String time = getIntent().getStringExtra("time");

        // Lấy các thành phần UI
        TextView titleView = findViewById(R.id.notificationTitle);
        TextView messageView = findViewById(R.id.notificationMessage);
        TextView timeView = findViewById(R.id.notificationTime);

        // Hiển thị thông tin thông báo vào các TextView
        titleView.setText(title);
        messageView.setText(message);
        timeView.setText(time);
    }

    // Phương thức xử lý sự kiện khi người dùng nhấn nút "Close"
    public void closeNotificationDetail(View view) {
        finish();  // Đóng activity và quay lại màn hình trước đó
    }
}

