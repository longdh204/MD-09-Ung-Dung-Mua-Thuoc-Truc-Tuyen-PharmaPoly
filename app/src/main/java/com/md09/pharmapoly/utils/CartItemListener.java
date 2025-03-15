package com.md09.pharmapoly.utils;

import com.md09.pharmapoly.Models.CartItem;

public interface CartItemListener {
    void onItemDeleted(CartItem cartItem);
    void onQuantityUpdated(CartItem cartItem);
}
