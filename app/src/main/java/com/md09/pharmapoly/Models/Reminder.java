package com.md09.pharmapoly.Models;

public class Reminder {
    private String medicineName;
    private int hour;
    private int minute;
    private boolean repeatDaily;

    public Reminder(String medicineName, int hour, int minute, boolean repeatDaily) {
        this.medicineName = medicineName;
        this.hour = hour;
        this.minute = minute;
        this.repeatDaily = repeatDaily;
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
}