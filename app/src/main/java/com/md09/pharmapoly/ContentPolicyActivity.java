package com.md09.pharmapoly;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContentPolicyActivity extends AppCompatActivity {
private ImageView btnContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content_policy);
        btnContent=findViewById(R.id.btn_content);
        btnContent.setOnClickListener(v -> {
            finish();
        });
    }
}