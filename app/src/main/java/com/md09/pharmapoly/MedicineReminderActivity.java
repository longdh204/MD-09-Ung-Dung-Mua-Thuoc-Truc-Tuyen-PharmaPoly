package com.md09.pharmapoly;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.CheckBox;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

public class MedicineReminderActivity extends AppCompatActivity {

    private EditText edtMedicineName;
    private TimePicker timePicker;
    private Button btnSetReminder;
    private RecyclerView recyclerView;
    private MedicineReminderAdapter adapter;
    private SharedPrefHelper sharedPrefHelper;
    private List<Reminder> reminderList;
    private CheckBox checkRepeatDaily, checkRepeatHourly;
    private List<Integer> selectedHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        edtMedicineName = findViewById(R.id.edtMedicineName);
        timePicker = findViewById(R.id.timePicker);
        btnSetReminder = findViewById(R.id.btnSetReminder);
        checkRepeatDaily = findViewById(R.id.checkRepeatDaily);
        checkRepeatHourly = findViewById(R.id.checkRepeatHourly);

        recyclerView = findViewById(R.id.recyclerView);

        // Khởi tạo SharedPrefHelper và lấy danh sách nhắc nhở từ SharedPreferences
        sharedPrefHelper = new SharedPrefHelper(this);
        reminderList = sharedPrefHelper.getReminders();

        // Khởi tạo RecyclerView và Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineReminderAdapter(reminderList, sharedPrefHelper);
        recyclerView.setAdapter(adapter);

        selectedHours = new ArrayList<>();  // Danh sách giờ đã chọn

        // Khi người dùng nhấn nút "Cài đặt nhắc nhở"
        btnSetReminder.setOnClickListener(v -> {
            String medicineName = edtMedicineName.getText().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (!medicineName.isEmpty()) {
                boolean repeatDaily = checkRepeatDaily.isChecked();
                boolean repeatHourly = checkRepeatHourly.isChecked();

                if (repeatHourly) {
                    // Hiển thị dialog để người dùng chọn nhiều giờ
                    showHourSelectionDialog();
                } else if (repeatDaily) {
                    // Lưu nhắc nhở lặp lại hàng ngày
                    sharedPrefHelper.saveReminder(medicineName, hour, minute, repeatDaily);
                    setReminder(hour, minute, medicineName, false, repeatDaily);
                    updateReminderList();
                }
            }
        });
    }

    // Hiển thị dialog chọn giờ
    private void showHourSelectionDialog() {
        final boolean[] checkedItems = new boolean[24]; // Mảng kiểm tra các giờ (0-23)

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn các giờ muốn thông báo")
                .setMultiChoiceItems(new CharSequence[]{
                        "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00",
                        "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
                        "20:00", "21:00", "22:00", "23:00"
                }, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedHours.add(which);  // Thêm giờ vào danh sách
                        } else {
                            selectedHours.remove(Integer.valueOf(which));  // Xóa giờ khỏi danh sách
                        }
                    }
                })
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Lưu các giờ đã chọn và tạo nhắc nhở
                        if (!selectedHours.isEmpty()) {
                            String medicineName = edtMedicineName.getText().toString();
                            for (int hour : selectedHours) {
                                sharedPrefHelper.saveReminderWithHours(medicineName, hour, 0, true);  // Lưu nhắc nhở cho các giờ
                                setReminder(hour, 0, medicineName, true, false);
                            }
                            updateReminderList();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .create()
                .show();
    }

    // Cập nhật lại danh sách nhắc nhở trong RecyclerView
    private void updateReminderList() {
        reminderList.clear();
        reminderList.addAll(sharedPrefHelper.getReminders());
        adapter.notifyDataSetChanged();
    }

    private void setReminder(int hour, int minute, String medicineName, boolean repeatHourly, boolean repeatDaily) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (repeatHourly) {
            WorkManager.getInstance(this).enqueue(createWorkRequest(medicineName, calendar));
        } else if (repeatDaily) {
            WorkManager.getInstance(this).enqueue(createWorkRequest(medicineName, calendar));
        }
    }

    private WorkRequest createWorkRequest(String medicineName, Calendar calendar) {
        Data inputData = new Data.Builder()
                .putString("medicine_name", medicineName)
                .build();

        return new OneTimeWorkRequest.Builder(MedicineReminderWorker.class)
                .setInputData(inputData)
                .setInitialDelay(calendar.getTimeInMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();
    }
}

