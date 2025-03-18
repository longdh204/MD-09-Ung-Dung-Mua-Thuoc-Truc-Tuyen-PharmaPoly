package com.md09.pharmapoly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class payment_card_manager_empty extends AppCompatActivity {
private ImageView btnBackPayment;
private Button btnAddPayment;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_card_manager_empty);
        btnBackPayment=findViewById(R.id.btn_back_payment_empty);
        btnBackPayment.setOnClickListener(v -> {
            finish();
        });
        btnAddPayment=findViewById(R.id.btn_add_payment);
        btnAddPayment.setOnClickListener(v -> {
            Intent intent = new Intent(payment_card_manager_empty.this,payment_card_manager.class);
            startActivity(intent);
        });
    }
}