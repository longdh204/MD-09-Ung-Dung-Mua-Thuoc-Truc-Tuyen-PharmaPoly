package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.Adapters.QuestionDetailAdapter;
import com.md09.pharmapoly.Models.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionDetailActivity extends AppCompatActivity {

    private RecyclerView questionRecyclerView;
    private QuestionDetailAdapter questionDetailAdapter;
    private List<Question> allQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);  // Layout cho Activity này

        // Khởi tạo RecyclerView
        questionRecyclerView = findViewById(R.id.questionRecyclerView);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách câu hỏi từ Intent
        allQuestions = (List<Question>) getIntent().getSerializableExtra("questions");

        // Kiểm tra xem có dữ liệu không
        if (allQuestions != null && !allQuestions.isEmpty()) {
            // Khởi tạo adapter và truyền câu hỏi vào
            questionDetailAdapter = new QuestionDetailAdapter(this, allQuestions);
            questionRecyclerView.setAdapter(questionDetailAdapter);
        } else {
            // Nếu không có câu hỏi, hiển thị thông báo
            Toast.makeText(this, "Không có câu hỏi nào!", Toast.LENGTH_SHORT).show();
        }
    }
}

