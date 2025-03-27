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

import com.md09.pharmapoly.Adapters.MedicineReminderAdapter;
import com.md09.pharmapoly.Models.Reminder;
import com.md09.pharmapoly.ui.view.activity.AlarmReceiver;
import com.md09.pharmapoly.ui.view.activity.MedicineReminderWorker;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MedicineReminderActivity extends AppCompatActivity {

    private EditText edtMedicineName;
    private TimePicker timePicker;
    private Button btnSetReminder;
    private RecyclerView recyclerView;
    private MedicineReminderAdapter adapter;
    private SharedPrefHelper sharedPrefHelper;
    private List<Reminder> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        edtMedicineName = findViewById(R.id.edtMedicineName);
        timePicker = findViewById(R.id.timePicker);
        btnSetReminder = findViewById(R.id.btnSetReminder);

        recyclerView = findViewById(R.id.recyclerView);

        // Khởi tạo SharedPrefHelper và lấy danh sách nhắc nhở từ SharedPreferences
        sharedPrefHelper = new SharedPrefHelper(this);
        reminderList = sharedPrefHelper.getReminders();

        // Khởi tạo RecyclerView và Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineReminderAdapter(reminderList, sharedPrefHelper);
        recyclerView.setAdapter(adapter);

        // Khi người dùng nhấn nút "Cài đặt nhắc nhở"
        btnSetReminder.setOnClickListener(v -> {
            String medicineName = edtMedicineName.getText().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (!medicineName.isEmpty()) {
                // Lưu nhắc nhở vào SharedPreferences
                boolean repeatDaily = true; // Điều chỉnh tuỳ theo yêu cầu lặp lại hàng ngày hay giờ
                sharedPrefHelper.saveReminder(medicineName, hour, minute, repeatDaily);

                // Tạo thông báo nhắc nhở
                setReminder(hour, minute, medicineName);

                // Cập nhật lại danh sách nhắc nhở trong RecyclerView
                updateReminderList();
            }
        });
    }

    // Hàm để cập nhật danh sách nhắc nhở trong RecyclerView
    private void updateReminderList() {
        reminderList.clear();
        reminderList.addAll(sharedPrefHelper.getReminders());
        adapter.notifyDataSetChanged();
    }

    private void setReminder(int hour, int minute, String medicineName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Tạo WorkRequest để lên lịch công việc nhắc nhở
        Data inputData = new Data.Builder()
                .putString("medicine_name", medicineName)
                .build();

        OneTimeWorkRequest reminderRequest = new OneTimeWorkRequest.Builder(MedicineReminderWorker.class)
                .setInputData(inputData)
                .setInitialDelay(calendar.getTimeInMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(reminderRequest);
    }
}
