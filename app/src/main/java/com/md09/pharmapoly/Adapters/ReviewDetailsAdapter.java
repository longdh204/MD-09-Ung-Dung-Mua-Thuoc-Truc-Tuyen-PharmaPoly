package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewDetailsAdapter extends RecyclerView.Adapter<ReviewDetailsAdapter.ViewHolder> {
    private Context context;
    private List<ProductReview> reviewList;

    public ReviewDetailsAdapter(Context context, List<ProductReview> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductReview review = reviewList.get(position);

        holder.reviewerName.setText(review.getUser().getFull_name());
        holder.reviewContent.setText(review.getReview());
        holder.rating.setText(review.getRating() + " sao");
//        holder.reviewDate.setText(review.getCreated_at());
        Date rawDate = review.getCreated_at();  // Bây giờ là Date thay vì String
        if (rawDate != null) {
            try {
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = outputFormat.format(rawDate);
                holder.reviewDate.setText(formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
                holder.reviewDate.setText("N/A");
            }
        } else {
            holder.reviewDate.setText("N/A");
        }

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerName, reviewContent, rating, reviewDate;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewContent = itemView.findViewById(R.id.reviewContent);
            rating = itemView.findViewById(R.id.rating);
            reviewDate = itemView.findViewById(R.id.reviewDate);
        }
    }
}
