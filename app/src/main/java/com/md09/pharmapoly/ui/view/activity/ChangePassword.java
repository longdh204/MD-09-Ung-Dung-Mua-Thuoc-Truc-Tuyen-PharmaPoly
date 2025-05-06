package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.PASSWORD_PATTERN;
import static com.md09.pharmapoly.utils.Constants.PHONE_NUMBER_KEY;
import static com.md09.pharmapoly.utils.Constants.UID_KEY;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    private EditText currentPassword, newPassword, confirmPassword;
    private CardView btnChangePassword;
    private RetrofitClient retrofitClient;
    private String phoneNumber, uid, token;
    private TextView change_pass_complete;
    private ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        initUI();

        Intent intent = getIntent();

        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
        User user = sharedPrefHelper.getUser();

        phoneNumber = user.getPhone_number();
        token = sharedPrefHelper.getToken();
        uid = intent.getStringExtra(UID_KEY);

        btnChangePassword.setOnClickListener(v -> changePassword());

        currentPassword.addTextChangedListener(textWatcher);
        newPassword.addTextChangedListener(textWatcher);
        confirmPassword.addTextChangedListener(textWatcher);

        btn_back.setOnClickListener(v -> {finish();});
    }

    private void changePassword() {
        String currentPass = currentPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (currentPass.isEmpty()) {
            currentPassword.setError(getString(R.string.error_enter_password));
            return;
        }
        if (!newPass.equals(confirmPass)) {
            confirmPassword.setError(getString(R.string.error_password_mismatch));
            return;
        }
        if (!isValidPassword(newPass)) {
            newPassword.setError(getString(R.string.password_policy));
            return;
        }

        ProgressDialogHelper.showLoading(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone_number", phoneNumber);
            jsonObject.put("password", currentPass);
            jsonObject.put("new_password", newPass);
            jsonObject.put("confirm_password", confirmPass);

            Log.d("JSON_LOG", jsonObject.toString());

        } catch (JSONException e) {
            return;
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), jsonObject.toString()
        );

        retrofitClient.callAPI().changePassword("Bearer " + token, requestBody)
                .enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(ChangePassword.this, getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show();
                            new SharedPrefHelper(ChangePassword.this).clearData();
                            startActivity(new Intent(ChangePassword.this, PhoneNumber.class));
                            finishAffinity();
                        } else {
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                                if (errorBody != null) {
                                    JSONObject jsonObject = new JSONObject(errorBody);
                                    String message = jsonObject.optString("message", getString(R.string.server_error));
                                    int statusCode = jsonObject.optInt("status", response.code());

                                    switch (statusCode) {
                                        case 403:
                                            currentPassword.setError(message);
                                            break;
                                        case 402:
                                            newPassword.setError(message);
                                            break;
                                        case 401:
                                            confirmPassword.setError(message);
                                            break;
                                        default:
                                            Toast.makeText(ChangePassword.this, message, Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                                else {
                                    Toast.makeText(ChangePassword.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ChangePassword.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                        Toast.makeText(ChangePassword.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    private void handleErrorResponse(int code) {
        switch (code) {
            case 404:
                Toast.makeText(ChangePassword.this, getString(R.string.error_user_not_found), Toast.LENGTH_SHORT).show();
                break;
            case 403:
                currentPassword.setError(getString(R.string.error_incorrect_password));
                break;
            case 402:
                newPassword.setError(getString(R.string.password_policy));
                break;
            case 401:
                confirmPassword.setError(getString(R.string.error_password_mismatch));
                break;
            default:
                Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            UpdateButtonState();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void UpdateButtonState() {
        String current_password = currentPassword.getText().toString().trim();
        String new_password = newPassword.getText().toString().trim();
        String confirm_password = confirmPassword.getText().toString().trim();

        boolean isValid = true;

        if (current_password.isEmpty()) {
            currentPassword.setError(getString(R.string.error_enter_password));
            isValid = false;
        }


        if (new_password.isEmpty()) {
            newPassword.setError(getString(R.string.error_enter_password));
            isValid = false;
        }
        if (!new_password.equals(confirm_password) && isValid) {
            confirmPassword.setError(getString(R.string.error_password_mismatch));
            isValid = false;
        }
        if (!isValidPassword(new_password) && isValid) {
            newPassword.setError(getString(R.string.password_policy));
            isValid = false;
        }

        int colorFromCard = btnChangePassword.getCardBackgroundColor().getDefaultColor();
        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);

        int colorFromText = change_pass_complete.getCurrentTextColor();
        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);

        btnChangePassword.setEnabled(isValid);

        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
        cardColorAnimator.setDuration(300);
        cardColorAnimator.addUpdateListener(animator ->
                btnChangePassword.setCardBackgroundColor((int) animator.getAnimatedValue())
        );
        cardColorAnimator.start();

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(animator ->
                change_pass_complete.setTextColor((int) animator.getAnimatedValue())
        );
        textColorAnimator.start();
    }

    private void initUI() {
        btn_back = findViewById(R.id.btn_back);
        currentPassword = findViewById(R.id.current_change_password);
        newPassword = findViewById(R.id.new_change_password);
        confirmPassword = findViewById(R.id.confirm_change_password);
        btnChangePassword = findViewById(R.id.btn_change_password);
        change_pass_complete = findViewById(R.id.change_pass_complete);
        retrofitClient = new RetrofitClient();
    }
}