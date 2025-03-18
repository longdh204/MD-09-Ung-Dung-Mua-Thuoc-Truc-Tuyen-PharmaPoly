package com.md09.pharmapoly.Models;

public class CartItem {
    private String _id;
    private String cart_id;
    private String product_id;
    private int quantity;
    private int price;
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
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "_id='" + _id + '\'' +
                ", cart_id='" + cart_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total_price=" + total_price +
                ", product=" + product +
                '}';
    }
}
