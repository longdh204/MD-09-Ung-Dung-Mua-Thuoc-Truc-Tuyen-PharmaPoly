package com.md09.pharmapoly.network;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.md09.pharmapoly.Models.NotificationItem;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.ui.view.activity.NotificationActivity;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    SharedPrefHelper sharedPrefHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String time = remoteMessage.getData().get("time");

            // Log dữ liệu nhận được
            Log.d("Firebase", "Received Notification - Title: " + title + ", Message: " + message + ", Time: " + time);

            // Lưu thông báo vào SharedPreferences
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
            NotificationItem notification = new NotificationItem(title, message, time, R.drawable.ic_bell, true);
            sharedPrefHelper.saveNotification(notification);  // Lưu thông báo vào SharedPreferences

            // Log để kiểm tra lưu dữ liệu
            Log.d("Firebase", "Notification saved successfully.");
        } else {
            Log.e("Firebase", "No data received in the message.");
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
