package com.md09.pharmapoly;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThayMatKhau extends AppCompatActivity {
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;
    private static final String URL_CHANGE_PASSWORD = "https://yourapi.com/user/change-password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thay_mat_khau);

        // Ánh xạ view
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(newPassword)) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 8 ký tự, 1 chữ hoa, 1 số, 1 ký tự đặc biệt!", Toast.LENGTH_LONG).show();
            return;
        }

        // Lấy token từ SharedPreferences (hoặc nơi lưu trữ của bạn)
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        StringRequest request = new StringRequest(Request.Method.PUT, URL_CHANGE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Toast.makeText(ThayMatKhau.this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ThayMatKhau.this, "Lỗi phản hồi từ server!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThayMatKhau.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", token);
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", currentPassword);
                params.put("new_password", newPassword);
                params.put("confirm_password", confirmPassword);
                return params;
            }
        };

        // Thêm request vào hàng đợi
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") &&
                password.matches(".*[0-9].*") && password.matches(".*[@#$%^&+=].*");
    }
}
