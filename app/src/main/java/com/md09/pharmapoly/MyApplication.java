package com.md09.pharmapoly;

import android.app.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    public static final String MEDICATION_CHANNEL_ID = "medication_channel";

    @Override
    public void onCreate() {
        super.onCreate();

        // Kiểm tra phiên bản Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo kênh thông báo nếu chưa có
            NotificationChannel channel = new NotificationChannel(
                    MEDICATION_CHANNEL_ID,
                    "Medication Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for medication reminders");

            // Đăng ký kênh thông báo với hệ thống
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
