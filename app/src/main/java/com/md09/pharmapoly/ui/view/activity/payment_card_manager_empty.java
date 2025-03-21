package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.R;

public class payment_card_manager_empty extends AppCompatActivity {
private Button payButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_card_manager_empty);
        // Ánh xạ Button payButton
        payButton = findViewById(R.id.payButton);
        if (payButton != null) {
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Khi người dùng nhấn nút "Thêm thẻ"
                    Intent intent = new Intent(payment_card_manager_empty.this, payment_card_manager.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.e("PaymentCardManager", "payButton không tìm thấy!");
        }
    }
}