package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private List<ProductReview> reviewList;

    public ReviewAdapter(Context context, List<ProductReview> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductReview review = reviewList.get(position);
        holder.reviewerName.setText(review.getUser_id());  // Tên người đánh giá
        holder.rating.setText(String.valueOf(review.getRating()));  // Đánh giá
        holder.reviewDate.setText(review.getCreate_at());  // Ngày đánh giá
        holder.reviewContent.setText(review.getReview());  // Nội dung đánh giá
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerName, rating, reviewDate, reviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            rating = itemView.findViewById(R.id.rating);
            reviewDate = itemView.findViewById(R.id.reviewDate);
            reviewContent = itemView.findViewById(R.id.reviewContent);
        }
    }
}
