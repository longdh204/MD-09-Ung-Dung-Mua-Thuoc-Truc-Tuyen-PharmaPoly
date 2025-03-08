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
import android.view.View;
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
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.ProgressDialogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPassword extends AppCompatActivity {


    private EditText
            edt_new_password,
            edt_confirm_password;
    private CardView btn_create_account;
    private RetrofitClient retrofitClient;
    private String phone_number,uid;
    private ImageButton btn_back;
    private TextView tv_complete, tv_error_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_password);

        initUI();

        Intent intent = getIntent();
        phone_number = intent.getStringExtra(PHONE_NUMBER_KEY);
        uid = intent.getStringExtra(UID_KEY);

        btn_create_account.setOnClickListener(v -> {
            String password = edt_new_password.getText().toString().trim();
            String confirm_password = edt_confirm_password.getText().toString().trim();

            if (password.isEmpty()) {
                edt_new_password.setError(getString(R.string.error_enter_password));
                return;
            }
            if (!password.equals(confirm_password)) {
                edt_confirm_password.setError(getString(R.string.error_password_mismatch));
                return;
            }
            if (!isValidPassword(password)) {
                edt_new_password.setError(getString(R.string.password_policy));
                return;
            }

            ProgressDialogHelper.showLoading(this);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uid", uid);
                jsonObject.put("phone_number", phone_number);
                jsonObject.put("password", password);
                jsonObject.put("confirm_password", confirm_password);
            } catch (JSONException e) {
                return;
            }

            RequestBody accountRequest = RequestBody.create(
                    MediaType.parse("application/json"), jsonObject.toString()
            );

            retrofitClient.callAPI().createAccount(accountRequest).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    ProgressDialogHelper.hideLoading();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Toast.makeText(SignupPassword.this, getString(R.string.account_created), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupPassword.this, PhoneNumber.class));
                            finishAffinity();
                        }
                    } else {
                        if (response.code() == 400) {
                            edt_new_password.setError(getString(R.string.password_policy));
                            edt_new_password.setFocusable(true);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    ProgressDialogHelper.hideLoading();
                    String message = getString(R.string.server_error);
                    Toast.makeText(SignupPassword.this, message, Toast.LENGTH_SHORT).show();
                }
            });


        });

        edt_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                UpdateButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                UpdateButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(SignupPassword.this,PhoneNumber.class));
            finish();
        });
    }


    private boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }
    private void UpdateButtonState() {
        String password = edt_new_password.getText().toString().trim();
        String confirm_password = edt_confirm_password.getText().toString().trim();

        boolean isValid = true;

        String errorMessage = "";

        if (password.isEmpty()) {
            errorMessage = "* " + getString(R.string.error_enter_password);
            isValid = false;
        } else if (!password.equals(confirm_password)) {
            errorMessage = "* " + getString(R.string.error_password_mismatch);
            isValid = false;
        } else if (!isValidPassword(password)) {
            errorMessage = "* " + getString(R.string.password_policy);
            isValid = false;
        }
        if (!isValid) {
            tv_error_message.setText(errorMessage);
            tv_error_message.setVisibility(View.VISIBLE);
        } else {
            tv_error_message.setVisibility(View.GONE);
        }


        int colorFromCard = btn_create_account.getCardBackgroundColor().getDefaultColor();
        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);

        int colorFromText = tv_complete.getCurrentTextColor();
        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);

        btn_create_account.setEnabled(isValid);

        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
        cardColorAnimator.setDuration(300);
        cardColorAnimator.addUpdateListener(animator ->
                btn_create_account.setCardBackgroundColor((int) animator.getAnimatedValue())
        );
        cardColorAnimator.start();

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(animator ->
                tv_complete.setTextColor((int) animator.getAnimatedValue())
        );
        textColorAnimator.start();
    }
    private void initUI() {
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_create_account = findViewById(R.id.btn_create_account);
        retrofitClient = new RetrofitClient();
        tv_complete = findViewById(R.id.tv_complete);
        tv_error_message = findViewById(R.id.tv_error_message);
        tv_error_message.setVisibility(View.GONE);
        btn_back = findViewById(R.id.btn_back);
    }
}