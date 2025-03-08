package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.PHONE_NUMBER_KEY;
import static com.md09.pharmapoly.utils.Constants.UID_KEY;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.utils.PopupHelper;
import com.md09.pharmapoly.utils.ProgressDialogHelper;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    private ImageButton btn_back;
    private TextView
            tv_countdown_timer,
            tv_phone_number,
            tv_verify,
            btn_resend_code;
    private CardView btn_verify;
    private EditText[] otpInputs;
    private FirebaseAuth auth;
    private String verification_id;
    private String uid;
    private String phone_number;
    private boolean isSendOtp = false;
    private boolean isCountdownTimerDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_phone);


        initUI();
        SetupFirebase();
        ShowPhoneNumber();

        SendOtp();
        SetupOtpInputs();
        SetupButton();

    }

    private void SetupFirebase() {
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode(Locale.getDefault().getLanguage());
    }

    private void SendOtp() {
        ProgressDialogHelper.showLoading(this);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone_number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                auth.signInWithCredential(credential)
                                        .addOnCompleteListener(VerifyPhone.this, task -> {
                                            ProgressDialogHelper.hideLoading();

                                            FirebaseUser user = auth.getCurrentUser();
                                            if (task.isSuccessful()) {
                                                uid = user.getUid();

                                                Intent intent = new Intent(VerifyPhone.this, SignupPassword.class);
                                                intent.putExtra(PHONE_NUMBER_KEY,phone_number);
                                                intent.putExtra(UID_KEY,phone_number);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(VerifyPhone.this, "Xác thực OTP thất bại!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                ProgressDialogHelper.hideLoading();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.e("OTP Error", "Gửi OTP thất bại: " + e.getMessage());
                                ProgressDialogHelper.hideLoading();
                                isSendOtp = false;
                                String message = getString(R.string.otp_request_limit);
                                PopupHelper.ShowPopup(VerifyPhone.this, "", message, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onCodeSent(@NonNull String id,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(id, token);
                                verification_id = id;
                                StartCountdownTimer();
                                isSendOtp = true;
                                Toast.makeText(VerifyPhone.this, "OTP đã được gửi!", Toast.LENGTH_SHORT).show();
                                ProgressDialogHelper.hideLoading();
                            }
                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void StartCountdownTimer() {
        isCountdownTimerDone = false;
        btn_resend_code.setEnabled(false);
        btn_resend_code.setTextColor(getColor(R.color.gray_DCD));

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                long minutes = secondsRemaining / 60;
                long seconds = secondsRemaining % 60;

                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tv_countdown_timer.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                isCountdownTimerDone = true;
                btn_resend_code.setEnabled(true);
                btn_resend_code.setTextColor(getColor(R.color.blue_CE4));
            }
        }.start();
    }

    private void SetupButton() {
        btn_back.setOnClickListener(v -> {
            finish();
        });

        btn_verify.setOnClickListener(v -> {
            ProgressDialogHelper.showLoading(this);
            StringBuilder otpCode = new StringBuilder();

            for (EditText otpInput : otpInputs) {
                otpCode.append(otpInput.getText().toString().trim());
            }

            String finalOtp = otpCode.toString();
            if (finalOtp.length() == otpInputs.length) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, finalOtp);

                auth.signInWithCredential(credential)
                        .addOnCompleteListener(this, task -> {
                            ProgressDialogHelper.hideLoading();

                            FirebaseUser user = auth.getCurrentUser();
                            if (task.isSuccessful()) {
                                uid = user.getUid();

                                Intent intent = new Intent(VerifyPhone.this, SignupPassword.class);
                                intent.putExtra(PHONE_NUMBER_KEY,phone_number);
                                intent.putExtra(UID_KEY,phone_number);
                                startActivity(intent);
                            } else {
                                Toast.makeText(VerifyPhone.this, "Xác thực OTP thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, getString(R.string.error_otp_required), Toast.LENGTH_SHORT).show();
            }
        });

        btn_resend_code.setOnClickListener(v -> {
            SendOtp();
        });
    }

    private void ShowPhoneNumber() {
        phone_number = getIntent().getStringExtra(PHONE_NUMBER_KEY);
        tv_phone_number.setText(getString(R.string.verification_code_sent_message) + "( " + phone_number + " )");
    }

    private void SetupOtpInputs() {
        for (int i = 0; i < otpInputs.length; i++) {
            final int index = i;

            otpInputs[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && index < otpInputs.length - 1) {
                        otpInputs[index + 1].requestFocus();
                    }
                    UpdateButtonState();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            otpInputs[index].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpInputs[index].getText().toString().isEmpty() && index > 0) {
                        otpInputs[index - 1].setText("");
                        otpInputs[index - 1].requestFocus();
                    }
                }
                return false;
            });
        }
    }
    private void UpdateButtonState() {
        if (!isSendOtp) return;
        StringBuilder otpCode = new StringBuilder();

        for (EditText otpInput : otpInputs) {
            otpCode.append(otpInput.getText().toString().trim());
        }

        String finalOtp = otpCode.toString();
        boolean isValid = finalOtp.length() == otpInputs.length;

        int colorFromCard = btn_verify.getCardBackgroundColor().getDefaultColor();
        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);

        int colorFromText = tv_verify.getCurrentTextColor();
        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);

        btn_verify.setEnabled(isValid);

        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
        cardColorAnimator.setDuration(300);
        cardColorAnimator.addUpdateListener(animator ->
                btn_verify.setCardBackgroundColor((int) animator.getAnimatedValue())
        );
        cardColorAnimator.start();

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(animator ->
                tv_verify.setTextColor((int) animator.getAnimatedValue())
        );
        textColorAnimator.start();
    }
    private void initUI() {
        btn_verify = findViewById(R.id.btn_verify);
        btn_resend_code = findViewById(R.id.btn_resend_code);
        btn_back = findViewById(R.id.btn_back);
        tv_countdown_timer = findViewById(R.id.tv_countdown_timer);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_verify = findViewById(R.id.tv_verify);

        otpInputs = new EditText[]{
                findViewById(R.id.edt_otp_1),
                findViewById(R.id.edt_otp_2),
                findViewById(R.id.edt_otp_3),
                findViewById(R.id.edt_otp_4),
                findViewById(R.id.edt_otp_5),
                findViewById(R.id.edt_otp_6)
        };

        btn_verify.setEnabled(false);
        btn_verify.setCardBackgroundColor(ContextCompat.getColor(this,R.color.gray_DCD));
        tv_verify.setTextColor(getColor(R.color.gray_E9E));
    }
}