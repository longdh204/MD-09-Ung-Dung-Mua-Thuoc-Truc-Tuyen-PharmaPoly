package com.md09.pharmapoly.network;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.md09.pharmapoly.Models.NotificationItem;
import com.md09.pharmapoly.R;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

import android.os.Build;
import android.app.NotificationChannel;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Kiểm tra xem thông báo có chứa dữ liệu không
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String time = remoteMessage.getData().get("time");

            // Lưu thông báo vào SharedPreferences
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
            sharedPrefHelper.saveReminder(title, 0, 0, true, false, new ArrayList<>());

            // Gửi thông báo đến UI (nếu cần)
            sendNotification(title, message, time);
        }

        // Nếu thông báo có phần thông báo (notification), xử lý thông báo đó
        if (remoteMessage.getNotification() != null) {
            // Xử lý thông báo theo nhu cầu (hiển thị thông báo trên thanh trạng thái)
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            sendNotification(title, message, null);
        }
    }


    // Hàm gửi thông báo đến thiết bị
    private void sendNotification(String title, String messageBody, String time) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (time != null) {
            notificationBuilder.setSubText("Thời gian: " + time);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

}
