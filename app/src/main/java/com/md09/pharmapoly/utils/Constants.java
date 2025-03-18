package com.md09.pharmapoly.utils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class Constants {
    public static final String BASE_URL = "https://pharmapoly-server.onrender.com/api/";
    public static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[1-9][0-9]{8}$");
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=|<>?{}\\[\\]~-]).{8,}$";
    public static final String PHONE_NUMBER_KEY = "PHONE_NUMBER";
    public static final String UID_KEY = "UID";

    public static final String USER_KEY = "USER";
    public static final String TOKEN_KEY = "TOKEN";
    public static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN";
    public static final String CART_ID_KEY = "CART_ID_KEY";
    public static final String PRODUCT_ADDED_TO_CART_KEY = "PRODUCT_ADDED_TO_CART";
    public static final String USER_PROFILE_UPDATED_KEY = "USER_PROFILE_UPDATED";

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int MAX_QUANTITY_PER_PRODUCT = 20;
    public static String formatCurrency(int amount, String symbol) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + symbol;
    }
}
