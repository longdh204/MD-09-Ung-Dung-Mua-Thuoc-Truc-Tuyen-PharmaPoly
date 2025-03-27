package com.md09.pharmapoly.ui.view.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.md09.pharmapoly.MedicineReminderActivity;
import com.md09.pharmapoly.MyApplication;
import com.md09.pharmapoly.R;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String medicineName = intent.getStringExtra("medicine_name");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Kiểm tra và tạo kênh thông báo nếu chưa có
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "medication_channel";
            CharSequence channelName = "Medication Reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            // Tạo kênh thông báo nếu chưa có
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        Notification notification = new NotificationCompat.Builder(context, MyApplication.MEDICATION_CHANNEL_ID)
                .setContentTitle("Đã đến giờ uống thuốc!")
                .setContentText("Đã đến giờ uống thuốc: " + medicineName)
                .setSmallIcon(R.drawable.ic_nhacuongthuoc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        // Hiển thị thông báo
        notificationManager.notify(0, notification);
    }
}




