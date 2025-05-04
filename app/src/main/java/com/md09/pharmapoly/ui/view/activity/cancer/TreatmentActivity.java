package com.md09.pharmapoly.ui.view.activity.cancer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.R;
import com.md09.pharmapoly.ui.view.activity.CancerInfoActivity;

public class TreatmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_treatment);
        setCategoryClickListeners();
    }

    private void setCategoryClickListeners() {
        findViewById(R.id.txt_cancer_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TreatmentActivity.this, CancerInfoActivity.class));
            }
        });
        findViewById(R.id.txt_cancer_types).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TreatmentActivity.this, CancerTypesActivity.class));
            }
        });

        findViewById(R.id.txt_screening).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TreatmentActivity.this, ScreeningActivity.class));
            }
        });

        findViewById(R.id.txt_treatment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TreatmentActivity.this, TreatmentActivity.class));
            }
        });

        findViewById(R.id.txt_prevention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TreatmentActivity.this, PreventionActivity.class));
            }
        });
    }


}
