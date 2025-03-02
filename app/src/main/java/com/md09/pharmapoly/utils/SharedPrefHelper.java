package com.md09.pharmapoly.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.md09.pharmapoly.data.model.User;

public class SharedPrefHelper {
    private static final String PREF_NAME = "UserPrefs";

    public static final String USER_KEY = "USER";
    public static final String TOKEN_KEY = "TOKEN";
    public static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    public SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void saveUser(com.md09.pharmapoly.data.model.User user, String token, String refreshToken) {
        editor.putString(USER_KEY, gson.toJson(user));
        editor.putString(TOKEN_KEY, token);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
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
    public void clearData() {
        editor.clear();
        editor.apply();
    }
}