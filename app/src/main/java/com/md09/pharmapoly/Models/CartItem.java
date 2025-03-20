package com.md09.pharmapoly.Models;

public class CartItem {
    private String _id;
    private String cart_id;
    private String product_id;
    private int quantity;
    private int discounted_price;
    private int original_price;
    private int total_price;
    private Product product;
    private boolean isSelected = false;
    public CartItem() {
    }

    public CartItem(String cart_id, String product_id, int quantity, int price, Product product) {
        this._id = "";
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.discounted_price = price;
        this.total_price = quantity * price;
        this.product = product;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(int discounted_price) {
        this.discounted_price = discounted_price;
    }

    public int getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(int original_price) {
        this.original_price = original_price;
    }

    public int getTotal_price() {
        return this.quantity * this.discounted_price;
    }


    @Override
    public String toString() {
        return "CartItem{" +
                "_id='" + _id + '\'' +
                ", cart_id='" + cart_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", quantity=" + quantity +
                ", price=" + discounted_price +
                ", total_price=" + total_price +
                ", product=" + product +
                ", isSelected=" + isSelected +
                '}';
    }
}
