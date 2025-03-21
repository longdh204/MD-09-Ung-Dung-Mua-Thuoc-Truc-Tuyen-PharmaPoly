package com.md09.pharmapoly;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DepositPolicyActivity extends AppCompatActivity {
private ImageView btnDeposit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deposit_policy);
        btnDeposit=findViewById(R.id.btn_deposit);
        btnDeposit.setOnClickListener(v ->{
            finish();
        });

    }
}