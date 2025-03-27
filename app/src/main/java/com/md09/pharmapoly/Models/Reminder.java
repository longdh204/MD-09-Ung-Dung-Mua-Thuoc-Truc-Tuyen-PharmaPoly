package com.md09.pharmapoly.Models;

import java.util.ArrayList;
import java.util.List;

public class Reminder {
    private String medicineName;
    private int hour;
    private int minute;
    private boolean repeatDaily;
    private boolean repeatHourly;
    private List<Integer> hours;  // Lưu danh sách giờ

    // Constructor cho nhắc nhở với nhiều giờ
    public Reminder(String medicineName, int hour, int minute, boolean repeatDaily, boolean repeatHourly, List<Integer> hours) {
        this.medicineName = medicineName;
        this.hour = hour;
        this.minute = minute;
        this.repeatDaily = repeatDaily;
        this.repeatHourly = repeatHourly;
        // Nếu hours là null, khởi tạo nó thành danh sách rỗng
        this.hours = (hours != null) ? hours : new ArrayList<>();
    }

    // Getter và setter
    public String getMedicineName() {
        return medicineName;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isRepeatDaily() {
        return repeatDaily;
    }

    public boolean isRepeatHourly() {
        return repeatHourly;
    }

    public List<Integer> getHours() {
        return hours;
    }
}


