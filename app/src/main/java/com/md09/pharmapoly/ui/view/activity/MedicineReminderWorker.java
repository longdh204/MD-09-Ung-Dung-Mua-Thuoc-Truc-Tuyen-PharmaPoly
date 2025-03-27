package com.md09.pharmapoly.ui.view.activity;

import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.md09.pharmapoly.R;

public class MedicineReminderWorker extends Worker {

    public MedicineReminderWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        // Lấy dữ liệu cần thiết từ `WorkRequest` nếu cần thiết
        String medicineName = getInputData().getString("medicine_name");

        // Tạo thông báo cho người dùng
        sendNotification(medicineName);

        return Result.success();
    }

    private void sendNotification(String medicineName) {
        // Gửi thông báo nhắc nhở người dùng uống thuốc
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "medication_channel")
                .setContentTitle("ĐỖ LONG hỏi bạn đã uống thuốc gì chưa nhỉ :(")
                .setContentText("Uống : " + medicineName + " mà bạn đã cài đặt đi không ĐỖ LONG cho ăn đấm bây giờ")
                .setSmallIcon(R.drawable.ic_nhacuongthuoc)
                .build();

        notificationManager.notify(0, notification);
    }
}
