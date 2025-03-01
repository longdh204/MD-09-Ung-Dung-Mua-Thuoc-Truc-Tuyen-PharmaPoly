package com.md09.pharmapoly.data.model;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("is_primary")
    private boolean isPrimary;  // Trường này sẽ cho biết ảnh có phải ảnh chính hay không

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Phương thức isPrimary() để kiểm tra ảnh có phải ảnh chính không
    public boolean isPrimary() {
        return isPrimary;
    }

    // Phương thức setPrimary để gán giá trị cho trường isPrimary
    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
