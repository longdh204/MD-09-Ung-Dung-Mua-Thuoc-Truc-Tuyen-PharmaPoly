package com.md09.pharmapoly.Adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private List<ProductReview> reviewList;
    public ReviewAdapter(Context context, List<ProductReview> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_review, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductReview review = reviewList.get(position);
        holder.reviewerName.setText(""+ review.getUser().getFull_name());
        holder.rating.setText(String.valueOf(review.getRating()));
        holder.reviewContent.setText(review.getReview());
        String rawDate = review.getCreated_at();
        if (rawDate != null && !rawDate.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                Date date = inputFormat.parse(rawDate);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = outputFormat.format(date);
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

