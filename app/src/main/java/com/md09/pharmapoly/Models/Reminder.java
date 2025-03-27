package com.md09.pharmapoly.Models;

import java.util.ArrayList;
import java.util.List;

public class Reminder {
    private String medicineName;
    private int hour;
    private int minute;
    private boolean repeatDaily;
    private List<Integer> hours; // Lưu danh sách giờ nếu lặp theo giờ

    // Constructor khi chỉ lặp hàng ngày
    public Reminder(String medicineName, int hour, int minute, boolean repeatDaily) {
        this.medicineName = medicineName;
        this.hour = hour;
        this.minute = minute;
        this.repeatDaily = repeatDaily;
        this.hours = new ArrayList<>(); // Để mặc định là danh sách rỗng
    }

    // Constructor khi lặp theo giờ
    public Reminder(String medicineName, int hour, int minute, boolean repeatHourly, List<Integer> hours) {
        this.medicineName = medicineName;
        this.hour = hour;
        this.minute = minute;
        this.repeatDaily = false; // Không phải lặp hàng ngày nếu là lặp theo giờ
        this.hours = hours != null ? hours : new ArrayList<>();
    }

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

    public List<Integer> getHours() {
        return hours;
    }
}