package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.PHONE_NUMBER_KEY;
import static com.md09.pharmapoly.utils.Constants.UID_KEY;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUpdate extends AppCompatActivity {

    private EditText edtFullName, edtPhoneNumber;
    private RadioButton rbMale, rbFemale;
    private CardView btnSaveProfile;
    private SharedPrefHelper sharedPrefHelper;
    private String phoneNumber, uid, token;
    private TextView profileUpdateComplete;
    private RetrofitClient retrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_update);

        initUI();

        Intent intent = getIntent();
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
        User user = sharedPrefHelper.getUser();

        phoneNumber = user.getPhone_number();
        token = sharedPrefHelper.getToken();

        uid = intent.getStringExtra(UID_KEY);

        loadUserData();

        btnSaveProfile.setOnClickListener(v -> updateUserProfile());

        edtFullName.addTextChangedListener(textWatcher);
        edtPhoneNumber.addTextChangedListener(textWatcher);
    }

    private void loadUserData() {
        User user = sharedPrefHelper.getUser();

        if (user != null) {
            edtFullName.setText(user.getFull_name());
            edtPhoneNumber.setText(user.getPhone_number());

            if (user.getGender() == 1) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }
        }
    }

    private void updateUserProfile() {
        String fullName = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        Integer gender = rbMale.isChecked() ? 1 : 0;

        if (fullName.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialogHelper.showLoading(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("phone_number", phoneNumber);
            jsonObject.put("full_name", fullName);
            jsonObject.put("gender", gender);
        } catch (JSONException e) {
            ProgressDialogHelper.hideLoading();
            Toast.makeText(this, getString(R.string.error_json), Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), jsonObject.toString()
        );

        retrofitClient.callAPI().updateProfile("Bearer " + token, requestBody).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                ProgressDialogHelper.hideLoading();

                Log.d("API_RESPONSE", "Status Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Response Body: " + new Gson().toJson(response.body()));

                    Toast.makeText(ProfileUpdate.this, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("API_RESPONSE", "Error Body: " + errorBody);
                    } catch (IOException e) {
                        Log.e("API_RESPONSE", "Error reading errorBody", e);
                    }

                    handleErrorResponse(response.code());
                }
            }


            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                ProgressDialogHelper.hideLoading();

                Log.e("API_RESPONSE", "API Call Failed", t);

                if (t instanceof IOException) {
                    Toast.makeText(ProfileUpdate.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileUpdate.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void handleErrorResponse(int code) {
        switch (code) {
            case 404:
                Toast.makeText(this, getString(R.string.error_user_not_found), Toast.LENGTH_SHORT).show();
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
            updateButtonState();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void updateButtonState() {
        boolean isValid = !edtFullName.getText().toString().trim().isEmpty() &&
                !edtPhoneNumber.getText().toString().trim().isEmpty();

        int colorFromCard = btnSaveProfile.getSolidColor();
        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);
        int colorFromText = profileUpdateComplete.getCurrentTextColor();
        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);

        btnSaveProfile.setEnabled(isValid);

        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
        cardColorAnimator.setDuration(300);
        cardColorAnimator.addUpdateListener(animator ->
                btnSaveProfile.setBackgroundColor((int) animator.getAnimatedValue())
        );
        cardColorAnimator.start();

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(animator ->
                profileUpdateComplete.setTextColor((int) animator.getAnimatedValue())
        );
    }

    private void initUI() {
        edtFullName = findViewById(R.id.update_profile_full_name);
        edtPhoneNumber = findViewById(R.id.update_profile_phone_number);
        rbMale = findViewById(R.id.update_profile_rb_male);
        rbFemale = findViewById(R.id.update_profile_rb_female);
        btnSaveProfile = findViewById(R.id.btn_update_profile);
        sharedPrefHelper = new SharedPrefHelper(this);
        retrofitClient = new RetrofitClient();
        profileUpdateComplete = findViewById(R.id.profile_update_complete);
    }
}
