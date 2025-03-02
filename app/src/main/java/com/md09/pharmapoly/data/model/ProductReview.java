package com.md09.pharmapoly.data.model;

import java.util.Date;

public class ProductReview {
    private String _id;
    private String product_id;
    private int rating;
    private String review;
    private Date created_at;

    public ProductReview() {
    }

    public ProductReview(String _id, String product_id, int rating, String review, Date created_at) {
        this._id = _id;
        this.product_id = product_id;
        this.rating = rating;
        this.review = review;
        this.created_at = created_at;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
