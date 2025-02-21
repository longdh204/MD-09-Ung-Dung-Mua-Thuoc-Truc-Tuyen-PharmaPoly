package com.md09.pharmapoly.utils;

import java.util.regex.Pattern;

public class Constants {
    public static final String BASE_URL = "https://pharmapoly-server.onrender.com/api/";
    public static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[1-9][0-9]{8}$");
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=|<>?{}\\[\\]~-]).{8,}$";
    public static final String PHONE_NUMBER_KEY = "PHONE_NUMBER";
    public static final String UID_KEY = "UID";
}
