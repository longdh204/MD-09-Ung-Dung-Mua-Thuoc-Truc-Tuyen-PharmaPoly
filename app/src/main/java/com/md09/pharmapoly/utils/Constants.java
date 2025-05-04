package com.md09.pharmapoly.utils;

import android.content.Context;
import android.content.res.Configuration;

import androidx.core.content.ContextCompat;

import com.md09.pharmapoly.Models.District;
import com.md09.pharmapoly.Models.Province;
import com.md09.pharmapoly.Models.Ward;
import com.md09.pharmapoly.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class Constants {
    public static final String BASE_URL = "https://pharmapoly-server.onrender.com/api/";
    //    public static final String BASE_URL = "http://192.168.147.223:3000/api/";
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
    public static final String CANCELED_KEY = "CANCELED";
    public static final String ORDER_KEY = "ORDER";
    public static final String LANGUAGE_KEY = "LANGUAGE";
    public static final String NOTIFICATION_READ_ALL_KEY = "NOTIFICATION_REAL_ALL";
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int MAX_QUANTITY_PER_PRODUCT = 20;

    public static final String formatCurrency(int amount, String symbol) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + symbol;
    }

    public static final <T> T findObjectById(ArrayList<T> list, String id) {
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

    public static final String getDisplayStatus(Context context, String status) {
        int stringResId;
        switch (status) {
            case "pending":
                stringResId = R.string.status_pending;
                break;
            case "confirmed":
                stringResId = R.string.status_confirmed;
                break;
            case "rejected":
                stringResId = R.string.rejected;
                break;
            case "ready_to_pick":
                stringResId = R.string.status_ready_to_pick;
                break;
            case "picking":
                stringResId = R.string.status_picking;
                break;
            case "picked":
                stringResId = R.string.status_picked;
                break;
            case "delivering":
                stringResId = R.string.status_delivering;
                break;
            case "money_collect_delivering":
                stringResId = R.string.status_money_collect_delivering;
                break;
            case "delivered":
                stringResId = R.string.status_delivered;
                break;
            case "delivery_fail":
                stringResId = R.string.status_delivery_fail;
                break;
            case "waiting_to_return":
                stringResId = R.string.status_waiting_to_return;
                break;
            case "return":
                stringResId = R.string.status_return;
                break;
            case "returned":
                stringResId = R.string.status_returned;
                break;
            case "return_fail":
                stringResId = R.string.status_return_fail;
                break;
            case "canceled":
                stringResId = R.string.status_canceled;
                break;
            default:
                return "Unknown";
        }
        return context.getString(stringResId);
    }

    public static final int getStatusColor(Context context, String status) {
        int colorResId;
        switch (status) {
            case "pending":
                colorResId = R.color.status_pending;
                break;
            case "confirmed":
                colorResId = R.color.status_confirmed;
                break;
            case "rejected":
                colorResId = R.color.status_canceled;
                break;
            case "ready_to_pick":
                colorResId = R.color.status_ready_to_pick;
                break;
            case "picking":
                colorResId = R.color.status_picking;
                break;
            case "picked":
                colorResId = R.color.status_picked;
                break;
            case "delivering":
                colorResId = R.color.status_delivering;
                break;
            case "money_collect_delivering":
                colorResId = R.color.status_money_collect_delivering;
                break;
            case "delivered":
                colorResId = R.color.status_delivered;
                break;
            case "delivery_fail":
                colorResId = R.color.status_delivery_fail;
                break;
            case "waiting_to_return":
                colorResId = R.color.status_waiting_to_return;
                break;
            case "return":
                colorResId = R.color.status_return;
                break;
            case "returned":
                colorResId = R.color.status_returned;
                break;
            case "return_fail":
                colorResId = R.color.status_return_fail;
                break;
            case "canceled":
                colorResId = R.color.status_canceled;
                break;
            default:
                colorResId = R.color.black;
        }
        return ContextCompat.getColor(context, colorResId);
    }

    public enum OrderStatusGroup {
        PROCESSING,
        SHIPPING,
        DELIVERED,
        //RETURNING,
        CANCELED
    }

    public static final void setLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
