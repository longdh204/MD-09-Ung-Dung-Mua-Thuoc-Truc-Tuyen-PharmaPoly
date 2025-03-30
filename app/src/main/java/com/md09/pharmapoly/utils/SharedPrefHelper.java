package com.md09.pharmapoly.utils;

import static com.md09.pharmapoly.utils.Constants.CART_ID_KEY;
import static com.md09.pharmapoly.utils.Constants.PAYMENT_METHOD_KEY;
import static com.md09.pharmapoly.utils.Constants.PRODUCT_ADDED_TO_CART_KEY;
import static com.md09.pharmapoly.utils.Constants.REFRESH_TOKEN_KEY;
import static com.md09.pharmapoly.utils.Constants.TOKEN_KEY;
import static com.md09.pharmapoly.utils.Constants.USER_KEY;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.md09.pharmapoly.Models.Reminder;
import com.md09.pharmapoly.data.model.User;

import java.lang.reflect.Type;
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
        sharedPreferences = context.getSharedPreferences("MedicineReminderPrefs", Context.MODE_PRIVATE);
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
        String method = sharedPreferences.getString(PAYMENT_METHOD_KEY, "COD");
        return PaymentMethod.fromString(method);
    }
    public void saveLanguage(String langCode) {
        editor.putString(Constants.LANGUAGE_KEY, langCode);
        editor.apply();
    }
    public String getLanguage() {
        return sharedPreferences.getString(Constants.LANGUAGE_KEY, "vi");
    }

    // Lưu thông tin thuốc và thời gian uống thuốc
    public void saveMedicineReminder(String medicineName, int hour, int minute) {
        editor.putString("medicine_name", medicineName);
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.apply();
    }

    // Lấy thông tin thuốc và thời gian uống
    public String getMedicineReminder() {
        String medicineName = sharedPreferences.getString("medicine_name", null);
        int hour = sharedPreferences.getInt("hour", -1);
        int minute = sharedPreferences.getInt("minute", -1);
        return (medicineName != null) ? medicineName + " at " + hour + ":" + minute : null;
    }

    // Lưu danh sách nhắc nhở
    // Lưu nhắc nhở hàng ngày (giữ nguyên vì không có thay đổi)
    // Lưu nhắc nhở với tất cả các giờ đã chọn
    public void saveReminder(String medicineName, int hour, int minute, boolean repeatDaily, boolean repeatHourly, List<Integer> selectedHours) {
        List<Reminder> reminders = getReminders();

        // Nếu selectedHours là null, khởi tạo nó thành danh sách rỗng
        if (selectedHours == null) {
            selectedHours = new ArrayList<>();
        }

        // Lưu nhắc nhở vào danh sách
        reminders.add(new Reminder(medicineName, hour, minute, repeatDaily, repeatHourly, selectedHours));

        // Lưu lại danh sách
        String json = gson.toJson(reminders);
        editor.putString("reminder_list", json);
        editor.apply();
    }

    // Lưu thông tin nhắc nhở với nhiều giờ
    public void saveReminderWithHours(String medicineName, int hour, int minute, boolean repeatDaily, boolean repeatHourly) {
        List<Reminder> reminders = getReminders();

        // Tạo danh sách giờ nếu lặp theo giờ
        List<Integer> hours = new ArrayList<>();
        if (repeatHourly) {
            hours.add(hour); // Thêm giờ vào danh sách (nếu muốn nhiều giờ, thêm nhiều lần)
        }

        // Lưu nhắc nhở
        reminders.add(new Reminder(medicineName, hour, minute, repeatDaily, repeatHourly, hours));

        // Lưu lại danh sách
        String json = gson.toJson(reminders);
        editor.putString("reminder_list", json);
        editor.apply();
    }


    // Lấy các giờ nhắc nhở theo tên thuốc
    public List<Integer> getReminderHours(String medicineName) {
        List<Reminder> reminders = getReminders();
        List<Integer> hours = new ArrayList<>();
        for (Reminder reminder : reminders) {
            if (reminder.getMedicineName().equals(medicineName)) {
                hours.add(reminder.getHour());
            }
        }
        return hours;
    }

    // Lấy danh sách nhắc nhở
    // Lấy danh sách nhắc nhở
    public List<Reminder> getReminders() {
        String json = sharedPreferences.getString("reminder_list", null);
        Type type = new TypeToken<List<Reminder>>() {
        }.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    // Xóa nhắc nhở theo chỉ mục
    public void deleteReminder(int index) {
        List<Reminder> reminders = getReminders();
        if (index >= 0 && index < reminders.size()) {
            reminders.remove(index);
            String json = gson.toJson(reminders);
            editor.putString("reminder_list", json);
            editor.apply();
        }
    }
}