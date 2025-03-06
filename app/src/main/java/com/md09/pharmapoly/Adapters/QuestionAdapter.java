package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Question;
import com.md09.pharmapoly.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private Context context;
    private List<Question> questionList;

    // Khởi tạo QuestionAdapter
    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        // Kiểm tra null và khởi tạo danh sách rỗng nếu null
        this.questionList = (questionList != null) ? questionList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_question để sử dụng
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questionList.get(position);

        // Gán nội dung câu hỏi vào TextView
        holder.questionContent.setText(question.getContent());

        // Kiểm tra câu hỏi có trả lời hay không và hiển thị câu trả lời đầu tiên nếu có
        if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
            holder.answerContent.setText(question.getAnswers().get(0).getContent());
        } else {
            // Nếu không có câu trả lời, hiển thị thông báo
            holder.answerContent.setText("No answers yet.");
        }
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng câu hỏi nhưng giới hạn tối đa là 3
        return Math.min(questionList.size(), 3);
    }

    // ViewHolder để ánh xạ các View trong item_question
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionContent, answerContent;

        public ViewHolder(View itemView) {
            super(itemView);
            // Ánh xạ các View
            questionContent = itemView.findViewById(R.id.questionContent);
            answerContent = itemView.findViewById(R.id.answerContent);
        }
    }
}
