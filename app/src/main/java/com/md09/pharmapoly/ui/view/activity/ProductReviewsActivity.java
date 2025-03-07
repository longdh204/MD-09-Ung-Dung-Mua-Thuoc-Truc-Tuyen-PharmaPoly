package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.QuestionAdapter;
import com.md09.pharmapoly.Adapters.ReviewAdapter;
import com.md09.pharmapoly.Adapters.ReviewDetailsAdapter;
import com.md09.pharmapoly.Models.Answer;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.Models.Question;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProductReviewsActivity extends AppCompatActivity {

    private RecyclerView reviewsRecyclerView;
    private ReviewDetailsAdapter reviewDetailsAdapter;
    private List<ProductReview> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_reviews);

        // Khởi tạo RecyclerView và Adapter
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewList = new ArrayList<>();

        reviewDetailsAdapter = new ReviewDetailsAdapter(this, reviewList);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(reviewDetailsAdapter);

        // Lấy tất cả review từ API và hiển thị
        fetchProductReviews();
    }

    private void fetchProductReviews() {
        // Cập nhật lại Adapter sau khi có dữ liệu
        reviewDetailsAdapter.notifyDataSetChanged();
    }
}




