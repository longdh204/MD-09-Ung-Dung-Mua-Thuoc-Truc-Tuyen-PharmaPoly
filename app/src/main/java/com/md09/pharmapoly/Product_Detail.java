package com.md09.pharmapoly;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Product_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Lấy các ProgressBar từ giao diện
        ProgressBar progressBar5Star = findViewById(R.id.progressBar5Star);
        ProgressBar progressBar4Star = findViewById(R.id.progressBar4Star);
        ProgressBar progressBar3Star = findViewById(R.id.progressBar3Star);
        ProgressBar progressBar2Star = findViewById(R.id.progressBar2Star);
        ProgressBar progressBar1Star = findViewById(R.id.progressBar1Star);


        // Thiết lập tỷ lệ phần trăm cho các sao
        progressBar5Star.setProgress(67);  // 67% cho 5 sao
        progressBar4Star.setProgress(20);  // 20% cho 4 sao
        progressBar3Star.setProgress(67);  // 67% cho 5 sao
        progressBar2Star.setProgress(20);  // 20% cho 4 sao
        progressBar1Star.setProgress(67);  // 67% cho 5 sao
    }
}