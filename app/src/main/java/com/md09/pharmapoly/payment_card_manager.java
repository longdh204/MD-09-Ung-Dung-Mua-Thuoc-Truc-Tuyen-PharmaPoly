package com.md09.pharmapoly;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class payment_card_manager extends AppCompatActivity {
    private ImageView btnBackPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_card_manager);
        btnBackPayment=findViewById(R.id.btn_back_payment);
        btnBackPayment.setOnClickListener(v -> {
            finish();
        });

    }
}