package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer_info);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Prepare articles
        articleList = new ArrayList<>();
        loadArticles();

        articleAdapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(articleAdapter);

        // Set click listeners for categories
        setCategoryClickListeners();
    }

    private void loadArticles() {
        // Add sample articles
        articleList.add(new Article("Tổng quan bệnh ung thư", "Nội dung bài viết tổng quan về bệnh ung thư..."));
        articleList.add(new Article("Đau đầu có phải là dấu hiệu ung thư?", "Nội dung bài viết về đau đầu..."));
        articleList.add(new Article("Dinh dưỡng trong điều trị ung thư", "Nội dung bài viết về dinh dưỡng..."));
        // Add more articles as needed
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