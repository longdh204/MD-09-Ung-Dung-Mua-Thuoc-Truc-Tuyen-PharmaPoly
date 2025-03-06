package com.md09.pharmapoly.Models;

public class Answer {
    private String _id;
    private String user_id;
    private String content;
    private String created_at;
    private UserQuestion user;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public UserQuestion getUser() {
        return user;
    }

    public void setUser(UserQuestion user) {
        this.user = user;
    }
}
