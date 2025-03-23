package com.md09.pharmapoly.utils;

import static com.md09.pharmapoly.utils.Constants.CART_ID_KEY;
import static com.md09.pharmapoly.utils.Constants.PAYMENT_METHOD_KEY;
import static com.md09.pharmapoly.utils.Constants.PRODUCT_ADDED_TO_CART_KEY;
import static com.md09.pharmapoly.utils.Constants.REFRESH_TOKEN_KEY;
import static com.md09.pharmapoly.utils.Constants.TOKEN_KEY;
import static com.md09.pharmapoly.utils.Constants.USER_KEY;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.md09.pharmapoly.data.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefHelper {
    private static final String PREF_NAME = "UserPrefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void saveUser(User user, String token, String refreshToken) {
        editor.putString(USER_KEY, gson.toJson(user));
        editor.putString(TOKEN_KEY, token);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.apply();
    }

    public void updateUser(User user) {
        editor.putString(USER_KEY, gson.toJson(user));
        editor.apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(USER_KEY, null);
        return (userJson != null) ? gson.fromJson(userJson, User.class) : null;
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
    }

    public void saveCartId(String cartId) {
        editor.putString(CART_ID_KEY, cartId);
        editor.apply();
    }

    public String getCartId() {
        return sharedPreferences.getString(CART_ID_KEY, null);
    }

    public void setBooleanState(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanState(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void resetBooleanState(String key) {
        editor.putBoolean(key, false);
        editor.apply();
    }

    public void clearData() {
        editor.clear();
        editor.apply();
    }
    public void saveSearchHistory(List<String> history) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> historySet = new HashSet<>(history);
        editor.putStringSet("search_history", historySet);
        editor.apply();
    }

    public List<String> getSearchHistory() {
        Set<String> historySet = sharedPreferences.getStringSet("search_history", new HashSet<>());
        return new ArrayList<>(historySet);
    }
    public void savePaymentMethod(PaymentMethod method) {
        editor.putString(PAYMENT_METHOD_KEY, method.getValue());
        editor.apply();
    }

    public PaymentMethod getPaymentMethod() {
        String method = sharedPreferences.getString(PAYMENT_METHOD_KEY, "cod");
        return PaymentMethod.fromString(method);
    }
}