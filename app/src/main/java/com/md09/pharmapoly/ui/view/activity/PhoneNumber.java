package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.PHONE_NUMBER_KEY;
import static com.md09.pharmapoly.utils.Constants.PHONE_PATTERN;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumber extends AppCompatActivity {

    private EditText edt_phone_number;
    private CardView btn_check_phone_number;
    private TextView tv_check_phone_number;
    private RetrofitClient retrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone_number);

        initUI();

        btn_check_phone_number.setEnabled(false);
        btn_check_phone_number.setCardBackgroundColor(ContextCompat.getColor(this,R.color.gray_DCD));

        edt_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateButtonState(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btn_check_phone_number.setOnClickListener(v -> {
            ProgressDialogHelper.showLoading(PhoneNumber.this);
            String phone_number = edt_phone_number.getText().toString();

            String phone_number_validate = validateAndFormatPhoneNumber(phone_number);
            if (phone_number_validate != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone_number", phone_number_validate);
                } catch (JSONException e) {
                    return;
                }

                RequestBody phoneRequest = RequestBody.create(
                        MediaType.parse("application/json"), jsonObject.toString()
                );

                retrofitClient.callAPI().checkPhone(phoneRequest).enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Intent intent = new Intent(PhoneNumber.this, LoginPassword.class);
                                intent.putExtra(PHONE_NUMBER_KEY, phone_number_validate);
                                startActivity(intent);
                            }
                        } else {
                            int statusCode = response.code();
                            String errorMessage = "";

                            switch (statusCode) {
                                case 400:
                                    errorMessage = getString(R.string.error_empty_phone);
                                    Toast.makeText(PhoneNumber.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    edt_phone_number.setError(errorMessage);
                                    edt_phone_number.requestFocus();
                                    break;

                                case 404:
                                    Intent intent = new Intent(PhoneNumber.this, VerifyPhone.class);
                                    intent.putExtra(PHONE_NUMBER_KEY, phone_number_validate);
                                    startActivity(intent);
                                    break;

                                default:
                                    errorMessage = getString(R.string.server_error);
                                    Toast.makeText(PhoneNumber.this, errorMessage , Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                        Toast.makeText(PhoneNumber.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(PhoneNumber.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
        ProgressDialogHelper.showLoading(this);
        String refreshToken = new SharedPrefHelper(this).getRefreshToken();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("refreshToken", refreshToken);
        retrofitClient.callAPI().refreshToken(requestBody).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                ProgressDialogHelper.hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        User user = response.body().getData();
                        String token = response.body().getToken();
                        String refreshToken = response.body().getRefreshToken();
                        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(PhoneNumber.this);
                        sharedPrefHelper.saveUser(user, token, refreshToken);

                        startActivity(new Intent(PhoneNumber.this, MainActivity.class));
                        finishAffinity();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                ProgressDialogHelper.hideLoading();

            }
        });
    }
    private void UpdateButtonState(String phoneNumber) {
        boolean isValid = validateAndFormatPhoneNumber(phoneNumber) != null;

        int colorFromCard = btn_check_phone_number.getCardBackgroundColor().getDefaultColor();
        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);

        int colorFromText = tv_check_phone_number.getCurrentTextColor();
        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);

        btn_check_phone_number.setEnabled(isValid);

        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
        cardColorAnimator.setDuration(300);
        cardColorAnimator.addUpdateListener(animator ->
                btn_check_phone_number.setCardBackgroundColor((int) animator.getAnimatedValue())
        );
        cardColorAnimator.start();

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(animator ->
                tv_check_phone_number.setTextColor((int) animator.getAnimatedValue())
        );
        textColorAnimator.start();
    }
    private String validateAndFormatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return null;
        }

        if (phoneNumber.startsWith("0")) {
            return "+84" + phoneNumber.substring(1);
        }
        return phoneNumber;
    }

    private void initUI() {
        retrofitClient = new RetrofitClient();
        edt_phone_number = findViewById(R.id.edt_phone_number);
        btn_check_phone_number = findViewById(R.id.btn_check_phone_number);
        tv_check_phone_number = findViewById(R.id.tv_check_phone_number);
    }
}