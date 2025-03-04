package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.R;
import com.md09.pharmapoly.welcome_login.DangNhapDangKy;

public class Onboarding_Screen_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding_screen_3);
    }
}