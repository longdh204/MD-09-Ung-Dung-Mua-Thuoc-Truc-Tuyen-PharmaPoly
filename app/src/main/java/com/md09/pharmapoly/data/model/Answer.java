package com.md09.pharmapoly.data.model;

import java.util.Date;

public class Answer {
    private String _id;
    private String question_id;
    private String user_id;
    private String content;
    private Date created_at;

    public Answer() {
    }

    public Answer(String _id, String question_id, String user_id, String content, Date created_at) {
        this._id = _id;
        this.question_id = question_id;
        this.user_id = user_id;
        this.content = content;
        this.created_at = created_at;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
