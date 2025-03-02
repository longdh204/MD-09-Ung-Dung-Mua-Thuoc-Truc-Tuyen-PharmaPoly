package com.md09.pharmapoly.data.model;

public class ProductImage {
    private String _id;
    private String product_id;
    private String image_url;
    private String is_primary;
    private int sort_order;

    public ProductImage() {
    }

    public ProductImage(String _id, String product_id, String image_url, String is_primary, int sort_order) {
        this._id = _id;
        this.product_id = product_id;
        this.image_url = image_url;
        this.is_primary = is_primary;
        this.sort_order = sort_order;
    }

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

    public String getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(String is_primary) {
        this.is_primary = is_primary;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }
}
