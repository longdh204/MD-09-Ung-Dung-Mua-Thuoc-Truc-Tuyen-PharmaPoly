package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.md09.pharmapoly.R;

public class ProfileOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_options);
        InitUI();
    }

    private void InitUI() {
        LinearLayout btnChangePassword = findViewById(R.id.btn_profile_change_password);
        LinearLayout btnUpdateProfile = findViewById(R.id.btn_profile_update_profile);

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileOptionsActivity.this, ChangePassword.class);
            startActivity(intent);
        });

        btnUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileOptionsActivity.this, ProfileUpdate.class);
            startActivity(intent);
        });
    }
}
