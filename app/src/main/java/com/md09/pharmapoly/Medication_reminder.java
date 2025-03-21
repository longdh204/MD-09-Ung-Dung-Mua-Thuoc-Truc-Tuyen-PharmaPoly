package com.md09.pharmapoly;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.WeekAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Medication_reminder extends AppCompatActivity {
    private TextView tvCurrentWeek;
    private RecyclerView recyclerViewWeek;
    private Calendar currentWeek;
    private WeekAdapter adapter;
    private List<Calendar> weekDays;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_reminder);

        tvCurrentWeek = findViewById(R.id.tvCurrentWeek);
        recyclerViewWeek = findViewById(R.id.recyclerViewWeek);
        ImageView btnPreviousWeek = findViewById(R.id.btnPreviousWeek);
        ImageView btnNextWeek = findViewById(R.id.btnNextWeek);

        currentWeek = Calendar.getInstance();
        weekDays = getWeekDays(currentWeek);

        adapter = new WeekAdapter(this, weekDays, Calendar.getInstance(), date -> {
            // Xử lý khi chọn ngày
        });

        recyclerViewWeek.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWeek.setAdapter(adapter);

        updateWeekLabel();

        btnPreviousWeek.setOnClickListener(v -> {
            currentWeek.add(Calendar.WEEK_OF_YEAR, -1);
            updateWeek();
        });

        btnNextWeek.setOnClickListener(v -> {
            currentWeek.add(Calendar.WEEK_OF_YEAR, 1);
            updateWeek();
        });
    }

    private List<Calendar> getWeekDays(Calendar week) {
        List<Calendar> days = new ArrayList<>();
        Calendar startOfWeek = (Calendar) week.clone();
        startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            Calendar day = (Calendar) startOfWeek.clone();
            day.add(Calendar.DAY_OF_MONTH, i);
            days.add(day);
        }

        return days;
    }

    private void updateWeek() {
        weekDays.clear();
        weekDays.addAll(getWeekDays(currentWeek));
        adapter.notifyDataSetChanged();
        updateWeekLabel();
    }

    private void updateWeekLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar today = Calendar.getInstance(); // Lấy ngày hiện tại
        tvCurrentWeek.setText("Hôm nay: " + sdf.format(today.getTime()));
    }

}
