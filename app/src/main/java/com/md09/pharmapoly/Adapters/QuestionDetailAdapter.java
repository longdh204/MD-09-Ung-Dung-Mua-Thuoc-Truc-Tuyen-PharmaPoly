package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.md09.pharmapoly.Models.Question;
import com.md09.pharmapoly.Models.Answer;
import com.md09.pharmapoly.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class QuestionDetailAdapter extends RecyclerView.Adapter<QuestionDetailAdapter.ViewHolder> {

    private Context context;
    private List<Question> questionList;

    public QuestionDetailAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questionList.get(position);

        // Hiển thị câu hỏi
        holder.questionContent.setText(question.getContent());

        // Hiển thị người hỏi
        if (question.getUserQuestion() != null) {
            holder.userName.setText(question.getUserQuestion().getFull_name());

            // Kiểm tra URL ảnh trước khi tải
            String avatarUrl = question.getUserQuestion().getAvatar_url();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Picasso.get().load(avatarUrl).into(holder.userAvatar);
            } else {
                // Nếu URL rỗng, sử dụng ảnh mặc định
                holder.userAvatar.setImageResource(R.drawable.ic_user); // Ảnh mặc định
            }
        }

        // Hiển thị thời gian câu hỏi được đăng
        holder.timePosted.setText(question.getCreated_at());

        // Hiển thị câu trả lời (nếu có)
        if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
            Answer answer = question.getAnswers().get(0);  // Chỉ hiển thị câu trả lời đầu tiên

            holder.answerContent.setText(answer.getContent());

            if (answer.getUser() != null) {
                holder.answerUserName.setText(answer.getUser().getFull_name());

                // Kiểm tra URL ảnh của người trả lời
                String answerAvatarUrl = answer.getUser().getAvatar_url();
                if (answerAvatarUrl != null && !answerAvatarUrl.isEmpty()) {
                    Picasso.get().load(answerAvatarUrl).into(holder.answerUserAvatar);
                } else {
                    // Nếu URL rỗng, sử dụng ảnh mặc định
                    holder.answerUserAvatar.setImageResource(R.drawable.ic_user); // Ảnh mặc định
                }
            }

            holder.answerTime.setText(answer.getCreated_at());
        } else {
            holder.answerContent.setText("No answers yet.");
        }
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionContent, userName, timePosted, answerContent, answerUserName, answerTime;
        ImageView userAvatar, answerUserAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            questionContent = itemView.findViewById(R.id.questionContent);
            userName = itemView.findViewById(R.id.userName);
            timePosted = itemView.findViewById(R.id.timePosted);
            answerContent = itemView.findViewById(R.id.answerContent);
            answerUserName = itemView.findViewById(R.id.answerUserName);
            answerTime = itemView.findViewById(R.id.answerTime);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            answerUserAvatar = itemView.findViewById(R.id.answerUserAvatar);
        }
    }
}
