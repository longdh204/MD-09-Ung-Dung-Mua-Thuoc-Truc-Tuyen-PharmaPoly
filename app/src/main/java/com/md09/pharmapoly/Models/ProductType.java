package com.md09.pharmapoly.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductType implements Serializable {
    private String _id;

    private String name;

    // Getters and Setters


    public ProductType() {
    }
//
    public ProductType(String _id, String name) {
        this._id = _id;
        this.name = name;
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
}
