package com.md09.pharmapoly.Models;

import java.util.Date;

public class Notification {
    private String _id;
    private String user_id;
    private String title;
    private String message;
    private boolean is_read;
    private Date created_at;
    private Date updated_at;
    public Notification() {
    }

    public Notification(String _id, String user_id, String title, String message, boolean is_read, Date created_at, Date updated_at) {
        this._id = _id;
        this.user_id = user_id;
        this.title = title;
        this.message = message;
        this.is_read = is_read;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
