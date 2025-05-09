package com.md09.pharmapoly.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Brand implements Serializable {

    private String _id;

    private String name;

    private String description;

    // Getters and Setters


    public Brand() {
    }

    public Brand(String _id, String name, String description) {
        this._id = _id;
        this.name = name;
        this.description = description;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
//
