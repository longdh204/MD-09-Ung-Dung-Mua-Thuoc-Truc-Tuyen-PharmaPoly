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
    private Category category;
    private Brand brand;
    private ProductType product_type;
    private List<ProductImage> images;
    private List<ProductSection> sections;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(String product_type_id) {
        this.product_type_id = product_type_id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ProductType getProduct_type() {
        return product_type;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
    }

    public List<ProductSection> getSections() {
        return sections;
    }

    public void setSections(List<ProductSection> sections) {
        this.sections = sections;
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
