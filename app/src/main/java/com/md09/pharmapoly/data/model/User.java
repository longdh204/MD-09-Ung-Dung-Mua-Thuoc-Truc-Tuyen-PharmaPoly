package com.md09.pharmapoly.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class User implements Serializable {

    private String _id;
    private String full_name;
    private Date date_of_birth;
    private int gender;
    private String phone_number;
    private String shipping_phone_number;
    private String google_id;
    private String address;
    private String avatar_url;
    private String uid;
    private int role;
    private int status;


    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", full_name='" + full_name + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", gender=" + gender +
                ", phone_number='" + phone_number + '\'' +
                ", shipping_phone_number='" + shipping_phone_number + '\'' +
                ", google_id='" + google_id + '\'' +
                ", address='" + address + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", uid='" + uid + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }

    public User(String full_name, int gender, String phone_number) {
        this.full_name = full_name;
        this.gender = gender;
        this.phone_number = phone_number;
    }
    public User(User user) {
        this._id = user._id;
        this.full_name = user.full_name;
        this.gender = user.gender;
        this.phone_number = user.shipping_phone_number;
        this.shipping_phone_number = user.shipping_phone_number;
        this.google_id = user.google_id;
        this.address = user.address;
        this.avatar_url = user.avatar_url;
        this.uid = user.uid;
        this.role = user.role;
        this.status = user.status;
        this.date_of_birth = user.date_of_birth;
    }

    public User(String _id, String full_name, Date date_of_birth, int gender, String phone_number, String shipping_phone_number, String google_id, String address, String avatar_url, String uid, int role, int status) {
        this._id = _id;
        this.full_name = full_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.phone_number = phone_number;
        this.shipping_phone_number = shipping_phone_number;
        this.google_id = google_id;
        this.address = address;
        this.avatar_url = avatar_url;
        this.uid = uid;
        this.role = role;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return gender == user.gender &&
                Objects.equals(full_name, user.full_name) &&
                Objects.equals(phone_number, user.phone_number) &&
                Objects.equals(shipping_phone_number, user.shipping_phone_number) &&
                Objects.equals(date_of_birth, user.date_of_birth) &&
                Objects.equals(address, user.address);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getShipping_phone_number() {
        return shipping_phone_number;
    }

    public void setShipping_phone_number(String shipping_phone_number) {
        this.shipping_phone_number = shipping_phone_number;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
