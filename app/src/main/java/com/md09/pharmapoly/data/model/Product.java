package com.md09.pharmapoly.data.model;

import java.util.Date;
import java.util.List;

public class Product {
    private String _id;
    private String name;
    private String category_id;
    private String brand_id;
    private String product_type_id;
    private int price;
    private String short_description;
    private String origin_country;
    private String manufacturer;
    private double average_rating;
    private int review_count;
    private Date created_at;
    private Category category;
    private Brand brand;
    private ProductType product_type;
    private List<ProductReview> images;

}
