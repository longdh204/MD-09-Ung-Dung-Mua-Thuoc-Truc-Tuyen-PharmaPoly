package com.md09.pharmapoly.utils;

import com.md09.pharmapoly.Models.District;
import com.md09.pharmapoly.Models.Province;
import com.md09.pharmapoly.Models.Ward;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Constants {
    public static final String BASE_URL = "http://192.168.1.72:3000/api/";
    public static final String GHN_URL = "https://dev-online-gateway.ghn.vn/";
    public static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[1-9][0-9]{8}$");
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=|<>?{}\\[\\]~-]).{8,}$";
    public static final String PHONE_NUMBER_KEY = "PHONE_NUMBER";
    public static final String UID_KEY = "UID";

    public static final String USER_KEY = "USER";
    public static final String TOKEN_KEY = "TOKEN";
    public static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN";
    public static final String CART_ID_KEY = "CART_ID";
    public static final String PRODUCT_ADDED_TO_CART_KEY = "PRODUCT_ADDED_TO_CART";
    public static final String USER_PROFILE_UPDATED_KEY = "USER_PROFILE_UPDATED";
    public static final String PAYMENT_METHOD_KEY = "PAYMENT_METHOD";

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int MAX_QUANTITY_PER_PRODUCT = 20;
    public static String formatCurrency(int amount, String symbol) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + symbol;
    }
    public static final  <T> T findObjectById(ArrayList<T> list, String id) {
        for (T item : list) {
            if (item instanceof Province) {
                Province province = (Province) item;
                if (String.valueOf(province.getProvinceID()).equals(id)) {
                    return item;
                }
            } else if (item instanceof District) {
                District district = (District) item;
                if (String.valueOf(district.getDistrictID()).equals(id)) {
                    return item;
                }
            } else if (item instanceof Ward) {
                Ward ward = (Ward) item;
                if (String.valueOf(ward.getWardCode()).equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }
}
