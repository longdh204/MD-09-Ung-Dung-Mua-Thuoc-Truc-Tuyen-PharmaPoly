package com.md09.pharmapoly.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("_id")
    private String id;
    private String name;
    private int price;
    @SerializedName("short_description")
    private String shortDescription;
    private String specification;
    @SerializedName("origin_country")
    private String originCountry;
    private String manufacturer;

    @SerializedName("average_rating")
    private float averageRating;

    @SerializedName("review_count")
    private int reviewCount;

    @SerializedName("create_at")
    private String createAt;

    @SerializedName("update_at")
    private String updateAt;

    private List<Image> images;  // Lưu danh sách ảnh

    public String getImageUrl() {
        if (images != null && !images.isEmpty()) {
            for (Image image : images) {
                if (image.isPrimary()) {
                    return image.getImageUrl();  // Lấy URL của ảnh chính
                }
            }
        }
        return null;  // Nếu không có ảnh chính, trả về null
    }
    public Product(String id, String name, int price, String shortDescription, String specification, String originCountry, String manufacturer, float averageRating, int reviewCount, String createAt, String updateAt, List<Image> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.shortDescription = shortDescription;
        this.specification = specification;
        this.originCountry = originCountry;
        this.manufacturer = manufacturer;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price +
                ", averageRating=" + averageRating + ", imageUrl='" + images + "'}";
    }
}
