package com.md09.pharmapoly;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.ui.view.activity.AlarmReceiver;
import com.md09.pharmapoly.ui.view.activity.MedicineReminderWorker;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MedicineReminderActivity extends AppCompatActivity {

    private EditText edtMedicineName;
    private TimePicker timePicker;
    private Button btnSetReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        edtMedicineName = findViewById(R.id.edtMedicineName);
        timePicker = findViewById(R.id.timePicker);
        btnSetReminder = findViewById(R.id.btnSetReminder);

        btnSetReminder.setOnClickListener(v -> {
            String medicineName = edtMedicineName.getText().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (!medicineName.isEmpty()) {
                setReminder(hour, minute, medicineName);
            }
        });
    }

    private void setReminder(int hour, int minute, String medicineName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Truyền dữ liệu vào InputData
        Data inputData = new Data.Builder()
                .putString("medicine_name", medicineName)  // Truyền tên thuốc
                .build();

        // Tạo WorkRequest để lên lịch công việc nhắc nhở
        OneTimeWorkRequest reminderRequest = new OneTimeWorkRequest.Builder(MedicineReminderWorker.class)
                .setInputData(inputData)  // Truyền dữ liệu vào WorkRequest
                .setInitialDelay(calendar.getTimeInMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();

        // Lên lịch công việc với WorkManager
        WorkManager.getInstance(this).enqueue(reminderRequest);
    }

}


