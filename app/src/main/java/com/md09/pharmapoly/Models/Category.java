package com.md09.pharmapoly.Models;

import java.io.Serializable;

public class Category implements Serializable {

    private String _id;
    private String name;
    private int imageRes;

    public Category() {
    }

    public Category(String _id, String name, int imageRes) {
        this._id = _id;
        this.name = name;
        this.imageRes = imageRes;
    }

    public Category(String name, int imageRes) {
        this.name = name;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public int getImageRes() {
        return imageRes;
    }
}
//
