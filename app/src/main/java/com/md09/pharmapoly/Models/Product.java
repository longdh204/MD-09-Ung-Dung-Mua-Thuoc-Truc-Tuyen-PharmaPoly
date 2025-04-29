package com.md09.pharmapoly.Models;  // Đảm bảo package này đúng

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private String _id;
    private String name;
    private String category_id;
    private String brand_id;
    //private String product_type_id;
    private int price;
    private Discount discount;
    private String short_description;
    private String specification;
    private String origin_country;
    private String manufacturer;
    private float average_rating;
    private int review_count;
    private String status;
    private String created_at;
    private Category category;
    private Brand brand;
    //private ProductType product_type;
    private List<ProductImage> images;
    private List<ProductProductType> product_types;
    private List<ProductSection> sections;
    // Lấy URL của ảnh chính

    private int selectedTypeIndex = 0;

    public int getSelectedTypeIndex() {
        return selectedTypeIndex;
    }

    public void setSelectedTypeIndex(int selectedTypeIndex) {
        this.selectedTypeIndex = selectedTypeIndex;
    }
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

    public Product(
            String _id,
            String name,
            String category_id,
            String brand_id,
//            String product_type_id,
            int price,
            Discount discount,
            String short_description,
            String specification,
            String origin_country,
            String manufacturer,
            float average_rating,
            int review_count,
            String create_at,
            Category category,
            Brand brand,
//            ProductType product_type,
            List<ProductImage> images,
            List<ProductSection> sections) {
        this._id = _id;
        this.name = name;
        this.category_id = category_id;
        this.brand_id = brand_id;
//        this.product_type_id = product_type_id;
        this.price = price;
        this.discount = discount;
        this.short_description = short_description;
        this.specification = specification;
        this.origin_country = origin_country;
        this.manufacturer = manufacturer;
        this.average_rating = average_rating;
        this.review_count = review_count;
        this.created_at = create_at;
        this.category = category;
        this.brand = brand;
//        this.product_type = product_type;
        this.images = images;
        this.sections = sections;
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

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
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

//    public String getProduct_type_id() {
//        return product_type_id;
//    }
//
//    public void setProduct_type_id(String product_type_id) {
//        this.product_type_id = product_type_id;
//    }

    public List<ProductProductType> getProduct_types() {
        return product_types;
    }

    public void setProduct_types(List<ProductProductType> product_types) {
        this.product_types = product_types;
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

//    public ProductType getProduct_type() {
//        return product_type;
//    }
//
//    public void setProduct_type(ProductType product_type) {
//        this.product_type = product_type;
//    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", category_id='" + category_id + '\'' +
                ", brand_id='" + brand_id + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", short_description='" + short_description + '\'' +
                ", specification='" + specification + '\'' +
                ", origin_country='" + origin_country + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", average_rating=" + average_rating +
                ", review_count=" + review_count +
                ", create_at='" + created_at + '\'' +
                ", category=" + category +
                ", brand=" + brand +
                ", images=" + images +
                ", product_types=" + product_types +
                ", sections=" + sections +
                '}';
    }
}
//