package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ArticleAdapter;
import com.md09.pharmapoly.Models.Article;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.ui.view.activity.cancer.CancerTypesActivity;
import com.md09.pharmapoly.ui.view.activity.cancer.PreventionActivity;
import com.md09.pharmapoly.ui.view.activity.cancer.ScreeningActivity;
import com.md09.pharmapoly.ui.view.activity.cancer.TreatmentActivity;

import java.util.ArrayList;
import java.util.List;

public class CancerInfoActivity extends AppCompatActivity {

    private List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancer_info);
        // Set click listeners for categories
        setCategoryClickListeners();
    }


    private void setCategoryClickListeners() {
        findViewById(R.id.txt_cancer_types).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình các loại ung thư
                startActivity(new Intent(CancerInfoActivity.this, CancerTypesActivity.class));
            }
        });

        findViewById(R.id.txt_screening).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình sàng lọc ung thư
                startActivity(new Intent(CancerInfoActivity.this, ScreeningActivity.class));
            }
        });

        findViewById(R.id.txt_treatment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình điều trị ung thư
                startActivity(new Intent(CancerInfoActivity.this, TreatmentActivity.class));
            }
        });

        findViewById(R.id.txt_prevention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình phòng ngừa ung thư
                startActivity(new Intent(CancerInfoActivity.this, PreventionActivity.class));
            }
        });
    }
}