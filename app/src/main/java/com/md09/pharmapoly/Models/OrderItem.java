package com.md09.pharmapoly.Models;

public class OrderItem {
    private String _id;
    private String product_product_type_id;
    private int quantity;
    private int price;
    private ProductProductType productType;
//    private Product product;
    public OrderItem() {
    }

    public OrderItem(String _id, String product_product_type_id, int quantity, int price, ProductProductType productType) {
        this._id = _id;
        this.product_product_type_id = product_product_type_id;
        this.quantity = quantity;
        this.price = price;
        this.productType = productType;
    }

    public ProductProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductProductType productType) {
        this.productType = productType;
    }

    public String getProduct_product_type_id() {
        return product_product_type_id;
    }

    public void setProduct_product_type_id(String product_product_type_id) {
        this.product_product_type_id = product_product_type_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

//    public String getProduct_id() {
//        return product_id;
//    }
//
//    public void setProduct_id(String product_id) {
//        this.product_id = product_id;
//    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
}
