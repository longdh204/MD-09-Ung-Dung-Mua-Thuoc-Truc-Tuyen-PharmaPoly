package com.md09.pharmapoly.Models;

public class NotificationModel {
    private String title;
    private String time;
    private int icon;
    private boolean isUnread;

    public NotificationModel(String title, String time, int icon, boolean isUnread) {
        this.title = title;
        this.time = time;
        this.icon = icon;
        this.isUnread = isUnread;
    }

    public String getTitle() { return title; }
    public String getTime() { return time; }
    public int getIcon() { return icon; }
    public boolean isUnread() { return isUnread; }
}

