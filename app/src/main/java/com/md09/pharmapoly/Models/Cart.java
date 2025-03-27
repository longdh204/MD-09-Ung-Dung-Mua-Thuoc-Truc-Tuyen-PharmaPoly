package com.md09.pharmapoly.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart {
    private String _id;
    private int total_price;
    private int total_items;
    private List<CartItem> cartItems;

    public Cart() {
    }

    public Cart(String _id, int total_price, int total_items, List<CartItem> cartItems) {
        this._id = _id;
        this.total_price = total_price;
        this.total_items = total_items;
        this.cartItems = cartItems;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "_id='" + _id + '\'' +
                ", total_price=" + total_price +
                ", total_items=" + total_items +
                ", cartItems=" + cartItems +
                '}';
    }
}
