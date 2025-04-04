package com.md09.pharmapoly.Models;

public class CartItem {
    private String _id;
    private String cart_id;
    private String product_product_type_id;
    private int quantity;
    private int original_price;
    private int total_price;
//    private Product product;
    private ProductProductType productType;
    private boolean isSelected = false;
    public CartItem() {
    }

    public CartItem(String cart_id, String product_id, int quantity, int price) {
        this._id = "";
        this.cart_id = cart_id;
        this.product_product_type_id = product_id;
        this.quantity = quantity;
        this.total_price = quantity * price;
//        this.product = product;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public ProductProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductProductType productType) {
        this.productType = productType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_product_type_id() {
        return product_product_type_id;
    }

    public void setProduct_product_type_id(String product_product_type_id) {
        this.product_product_type_id = product_product_type_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public int getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(int original_price) {
        this.original_price = original_price;
    }

    public int getTotal_price() {
        return this.quantity * this.original_price;
    }


    @Override
    public String toString() {
        return "CartItem{" +
                "_id='" + _id + '\'' +
                ", cart_id='" + cart_id + '\'' +
                ", product_product_type_id='" + product_product_type_id + '\'' +
                ", quantity=" + quantity +
                ", original_price=" + original_price +
                ", total_price=" + total_price +
//                ", product=" + product +
                ", isSelected=" + isSelected +
                '}';
    }
}
