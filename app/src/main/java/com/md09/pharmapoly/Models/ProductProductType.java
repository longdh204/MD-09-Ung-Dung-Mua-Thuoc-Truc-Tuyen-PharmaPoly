package com.md09.pharmapoly.Models;

import java.io.Serializable;

public class ProductProductType implements Serializable {
    private String _id;
    private String product_id;
    private String product_type_id;
    private int price;
    private String name;
    private Product product;
    private ProductType productType;
    public ProductProductType() {
    }

    public ProductProductType(String _id, String product_id, String product_type_id, int price, String name) {
        this._id = _id;
        this.product_id = product_id;
        this.product_type_id = product_type_id;
        this.price = price;
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
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

    public String getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(String product_type_id) {
        this.product_type_id = product_type_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
