package com.md09.pharmapoly.ui.view.activity.cancer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.R;
import com.md09.pharmapoly.ui.view.activity.CancerInfoActivity;
import com.md09.pharmapoly.ui.view.activity.cancer.ScreeningActivity;
import com.md09.pharmapoly.ui.view.activity.cancer.TreatmentActivity;
import com.md09.pharmapoly.ui.view.activity.cancer.PreventionActivity;
import com.md09.pharmapoly.ui.view.activity.MainActivity;

public class CancerTypesActivity extends AppCompatActivity {
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_cancer_types);
        setCategoryClickListeners();
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(CancerTypesActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("open_home", true); // Truyền thêm flag nếu cần
            startActivity(intent);
            finish();
        });
    }

    private void setCategoryClickListeners() {
        findViewById(R.id.txt_cancer_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CancerTypesActivity.this, CancerInfoActivity.class));
            }
        });
        findViewById(R.id.txt_cancer_types).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình các loại ung thư
                startActivity(new Intent(CancerTypesActivity.this, CancerTypesActivity.class));
            }
        });

        findViewById(R.id.txt_screening).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình sàng lọc ung thư
                startActivity(new Intent(CancerTypesActivity.this, ScreeningActivity.class));
            }
        });

        findViewById(R.id.txt_treatment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình điều trị ung thư
                startActivity(new Intent(CancerTypesActivity.this, TreatmentActivity.class));
            }
        });

        findViewById(R.id.txt_prevention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình phòng ngừa ung thư
                startActivity(new Intent(CancerTypesActivity.this, PreventionActivity.class));
            }
        });
    }
}