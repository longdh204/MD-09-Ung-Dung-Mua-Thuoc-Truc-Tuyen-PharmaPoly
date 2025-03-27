package com.md09.pharmapoly.Models;

import java.io.Serializable;

public class ProductImage implements Serializable {

    private String _id;
    private String product_id;
    private String image_url;
    private boolean is_primary;
    private float sort_order;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    // Thay đổi từ isIs_primary thành isPrimary để dễ sử dụng
    public boolean isPrimary() {
        return is_primary;
    }

    public void setIs_primary(boolean is_primary) {
        this.is_primary = is_primary;
    }

    public float getSort_order() {
        return sort_order;
    }

    public void setSort_order(float sort_order) {
        this.sort_order = sort_order;
    }
}
