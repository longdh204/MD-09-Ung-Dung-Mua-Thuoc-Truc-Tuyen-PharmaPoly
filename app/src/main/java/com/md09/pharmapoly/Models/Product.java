package com.md09.pharmapoly.Models;  // Đảm bảo package này đúng

import java.util.List;

public class Product {
    private String _id;
    private String name;
    private String category_id;
    private String brand_id;
    private String product_type_id;
    private int price;
    private String short_description;
    private String specification;
    private String origin_country;
    private String manufacturer;

    private float average_rating;

    private int review_count;

    private String create_at;

    private List<ProductImage> images;  // Lưu danh sách ảnh

    // Lấy URL của ảnh chính
    public String getImageUrl() {
        if (images != null && !images.isEmpty()) {
            for (ProductImage image : images) {
                if (image.isPrimary()) {
                    return image.getImage_url();  // Lấy URL của ảnh chính
                }
            }
        }
        return null;  // Nếu không có ảnh chính, trả về null
    }

    // Các getter và setter cho các trường khác
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{id='" + _id + "', name='" + name + "', price=" + price +
                ", averageRating=" + average_rating + ", imageUrl='" + images + "'}";
    }
}
