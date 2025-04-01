package com.md09.pharmapoly.Models;

public class NotificationItem {
    private String title;
    private String message;
    private String time;
    private int icon;
    private boolean isUnread;

    // Constructor
    public NotificationItem(String title, String message, String time, int icon, boolean isUnread) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.icon = icon;
        this.isUnread = isUnread;
    }

    // Getter and Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }
}
