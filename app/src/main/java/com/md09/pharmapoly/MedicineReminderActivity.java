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

import androidx.activity.EdgeToEdge;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MedicineReminderActivity extends AppCompatActivity implements MedicineReminderAdapter.UpdateReminderListener {

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
        EdgeToEdge.enable(this);

        edtMedicineName = findViewById(R.id.edtMedicineName);
        timePicker = findViewById(R.id.timePicker);
        btnSetReminder = findViewById(R.id.btnSetReminder);
        checkRepeatDaily = findViewById(R.id.checkRepeatDaily);
        checkRepeatHourly = findViewById(R.id.checkRepeatHourly);

        recyclerView = findViewById(R.id.recyclerView);

        // Khởi tạo SharedPrefHelper và lấy danh sách nhắc nhở từ SharedPreferences
        sharedPrefHelper = new SharedPrefHelper(this);
        reminderList = sharedPrefHelper.getReminders();

        // Khởi tạo RecyclerView và Adapter, truyền "this" vì Activity đã implements UpdateReminderListener
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineReminderAdapter(reminderList, sharedPrefHelper, this);
        recyclerView.setAdapter(adapter);

        selectedHours = new ArrayList<>();  // Danh sách giờ đã chọn

        // Khi người dùng nhấn nút "Cài đặt nhắc nhở"
        btnSetReminder.setOnClickListener(v1 -> {
            String updatedMedicineName = edtMedicineName.getText().toString();
            int updatedHour = timePicker.getHour();
            int updatedMinute = timePicker.getMinute();

            boolean repeatDaily = checkRepeatDaily.isChecked();
            boolean repeatHourly = checkRepeatHourly.isChecked();

            if (!updatedMedicineName.isEmpty()) {
                // Kiểm tra nếu tên thuốc đã tồn tại
                if (isMedicineNameDuplicate(updatedMedicineName)) {
                    Toast.makeText(MedicineReminderActivity.this, "Tên thông báo đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu nhắc nhở sau khi chỉnh sửa
                if (repeatHourly || repeatDaily) {
                    sharedPrefHelper.saveReminder(updatedMedicineName, updatedHour, updatedMinute, repeatDaily, repeatHourly, selectedHours);
                    setReminder(updatedHour, updatedMinute, updatedMedicineName, repeatHourly, repeatDaily);
                }
                updateReminderList();  // Cập nhật danh sách sau khi chỉnh sửa
            }
        });
        checkRepeatHourly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiển thị dialog để người dùng chọn nhiều giờ
                showHourSelectionDialog();
            } else {
                // Nếu bỏ chọn, xóa danh sách các giờ đã chọn
                selectedHours.clear();
            }
        });

    }

    private void showHourSelectionDialog() {
        final boolean[] checkedItems = new boolean[24]; // Mảng kiểm tra các giờ (0-23)

        // Tạo AlertDialog cho phép người dùng chọn nhiều giờ
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn các giờ muốn thông báo")
                .setMultiChoiceItems(new CharSequence[]{
                        "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00",
                        "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
                        "20:00", "21:00", "22:00", "23:00"
                }, checkedItems, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        selectedHours.add(which);  // Thêm giờ vào danh sách
                    } else {
                        selectedHours.remove(Integer.valueOf(which));  // Xóa giờ khỏi danh sách
                    }
                })
                .setPositiveButton("Lưu", (dialog, id) -> {
                    // Lưu các giờ đã chọn và tạo nhắc nhở
                    if (!selectedHours.isEmpty()) {
                        String medicineName = edtMedicineName.getText().toString();
                        // Lưu nhắc nhở chỉ với 1 item duy nhất chứa tất cả các giờ
                        sharedPrefHelper.saveReminder(medicineName, timePicker.getHour(), timePicker.getMinute(), checkRepeatDaily.isChecked(), true, selectedHours);
                        updateReminderList();  // Cập nhật lại danh sách nhắc nhở
                    }
                })
                .setNegativeButton("Hủy", null)
                .create()
                .show();
    }

    // Phương thức từ interface gọi khi nhấn nút "Cập nhật"
    @Override
    public void onUpdateReminder(Reminder reminder) {
        edtMedicineName.setText(reminder.getMedicineName());
        timePicker.setHour(reminder.getHour());
        timePicker.setMinute(reminder.getMinute());
        // Hiển thị thông tin nhắc nhở trong các trường để chỉnh sửa
    }

    // Kiểm tra tên thông báo đã tồn tại chưa
    private boolean isMedicineNameDuplicate(String name) {
        for (Reminder reminder : reminderList) {
            if (reminder.getMedicineName().equals(name)) {
                return true;
            }
        }
        return false;
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

