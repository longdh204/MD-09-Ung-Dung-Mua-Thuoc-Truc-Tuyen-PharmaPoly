package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.PHONE_NUMBER_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.messaging.FirebaseMessaging;
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

public class LoginPassword extends AppCompatActivity {

    private EditText edt_password;
    private CardView btn_login;
    private TextView tv_phone_number,btn_forgotPassword;
    private RetrofitClient retrofitClient;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_password);

        initUI();

        String phoneNumber = getIntent().getStringExtra(PHONE_NUMBER_KEY);
        tv_phone_number.setText("( " + phoneNumber + " )");

        btn_back.setOnClickListener(v -> {
            finish();
        });
        btn_forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPassword.this, VerifyPhone.class);
            intent.putExtra(PHONE_NUMBER_KEY, phoneNumber);
            intent.putExtra("purpose", "forgot_password");
            startActivity(intent);
        });
        btn_login.setOnClickListener(v -> {
            ProgressDialogHelper.showLoading(LoginPassword.this);
            String password = edt_password.getText().toString().trim();

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("FCM", "Fetching FCM token failed", task.getException());
                    return;
                }
                String token = task.getResult();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone_number", phoneNumber);
                    jsonObject.put("password", password);
                    jsonObject.put("fcm_token", token);
                } catch (JSONException e) {
                    return;
                }

                RequestBody phoneRequest = RequestBody.create(
                        MediaType.parse("application/json"), jsonObject.toString()
                );
                retrofitClient.callAPI().login(phoneRequest).enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                User user = response.body().getData();
                                String token = response.body().getToken();
                                String refreshToken = response.body().getRefreshToken();
                                SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(LoginPassword.this);
                                sharedPrefHelper.saveUser(user, token, refreshToken);

                                startActivity(new Intent(LoginPassword.this, MainActivity.class));
                                finishAffinity();
                            }
                        } else {
                            String message = "";
                            switch (response.code()) {
                                case 400:
                                    message = getString(R.string.error_phone_password_required);
                                    edt_password.setError(message);
                                    edt_password.setFocusable(true);
                                    break;
                                case 404:
                                    message = getString(R.string.error_user_not_found);
                                    startActivity(new Intent(LoginPassword.this, PhoneNumber.class));
                                    finish();
                                    break;
                                case 401:
                                    message = getString(R.string.error_invalid_credentials);
                                    edt_password.setError(message);
                                    edt_password.setFocusable(true);
                                    break;
                                default:
                                    message = getString(R.string.server_error);
                                    break;
                            }
                            Toast.makeText(LoginPassword.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                        String message = getString(R.string.server_error);
                        Toast.makeText(LoginPassword.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            });


//            retrofitClient.callAPI().login(phoneRequest).enqueue(new Callback<ApiResponse<Void>>() {
//                @Override
//                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
//                    ProgressDialogHelper.hideLoading();
//                    if (response.isSuccessful()) {
//                        if (response.body().getStatus() == 200) {
//                            startActivity(new Intent(LoginPassword.this, MainActivity.class));
//                        }
//                    } else {
//                        String message = "";
//                        switch (response.code()) {
//                            case 400:
//                                message = getString(R.string.error_phone_password_required);
//                                edt_password.setError(message);
//                                edt_password.setFocusable(true);
//                                break;
//                            case 404:
//                                message = getString(R.string.error_user_not_found);
//                                startActivity(new Intent(LoginPassword.this, PhoneNumber.class));
//                                finish();
//                                break;
//                            case 401:
//                                message = getString(R.string.error_invalid_credentials);
//                                edt_password.setError(message);
//                                edt_password.setFocusable(true);
//                                break;
//                            default:
//                                message = getString(R.string.server_error);
//                                break;
//                        }
//                        Toast.makeText(LoginPassword.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
//                    ProgressDialogHelper.hideLoading();
//                    String message = getString(R.string.server_error);
//                    Toast.makeText(LoginPassword.this, message, Toast.LENGTH_SHORT).show();
//                }
//            });
        });
    }

    private void initUI() {
        retrofitClient = new RetrofitClient();
        btn_forgotPassword = findViewById(R.id.btn_forgotPassword);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        btn_back = findViewById(R.id.btn_back);
    }
}