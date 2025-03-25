package com.md09.pharmapoly.Models;

public class ItemCategoryHome {

    private String name; // Tên của category
    private int iconResId; // Resource ID của icon (hình ảnh)

    // Constructor
    public ItemCategoryHome(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    // Getter cho tên category
    public String getName() {
        return name;
    }

    // Setter cho tên category
    public void setName(String name) {
        this.name = name;
    }

    // Getter cho iconResId
    public int getIconResId() {
        return iconResId;
    }

    // Setter cho iconResId
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}

