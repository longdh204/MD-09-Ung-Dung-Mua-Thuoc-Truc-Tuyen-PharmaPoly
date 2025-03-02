package com.md09.pharmapoly.data.model;

import java.util.Date;

public class Question {
    private String _id;
    private String user_id;
    private String product_id;
    private String content;
    private int status;
    private Date created_at;

    public Question() {
    }

    public Question(String _id, String user_id, String product_id, String content, int status, Date created_at) {
        this._id = _id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.content = content;
        this.status = status;
        this.created_at = created_at;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
