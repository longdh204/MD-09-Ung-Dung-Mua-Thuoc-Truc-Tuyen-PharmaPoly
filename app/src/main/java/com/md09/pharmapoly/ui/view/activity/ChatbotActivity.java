package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.md09.pharmapoly.Adapters.ChatAdapter;
import com.md09.pharmapoly.Models.ChatMessage;
import com.md09.pharmapoly.R;

import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText inputMessage;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private LinearLayout suggestionLayout, suggestionLayout2;
    private DatabaseReference chatbotRef;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbot);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            finish();
        });
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);
        suggestionLayout = findViewById(R.id.suggestionLayout);
        suggestionLayout2 = findViewById(R.id.suggestionLayout2);
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        chatbotRef = FirebaseDatabase.getInstance().getReference("chatbot");

        sendButton.setOnClickListener(v -> sendMessage());

        // Thêm các câu hỏi gợi ý
        addSuggestionButtons();
    }

    private void sendMessage() {
        String message = inputMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            chatMessages.add(new ChatMessage(message, true));
            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
            inputMessage.setText("");
            fetchChatbotResponse(message);
        }
    }

    private void fetchChatbotResponse(String userMessage) {
        chatbotRef.child(userMessage.toLowerCase().trim()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String botResponse = snapshot.getValue(String.class);
                    chatMessages.add(new ChatMessage(botResponse, false));
                } else {
                    chatMessages.add(new ChatMessage("Hãy mô tả triệu chứng của bạn, ví dụ: ho nhiều, đau đầu, sốt cao...", false));
                }
            } else {
                chatMessages.add(new ChatMessage("Lỗi kết nối đến chatbot!", false));
            }
            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
        });
    }

    private void addSuggestionButtons() {
        String[] suggestions = {"xin chào", "tôi bị sốt", "đau đầu", "cảm cúm", "tôi bị dị ứng", "tôi mất ngủ"};
        String[] suggestions2 = {"tôi đang bị bệnh", "huyết áp cao", "đau bụng trên rốn là bị gì", "đau lưng"};

        for (String suggestion : suggestions) {
            Button button = new Button(this);
            button.setText(suggestion);
            button.setAllCaps(false);
            button.setPadding(16, 8, 16, 8);
            button.setBackgroundResource(R.drawable.bg_edittext_rounded);
            button.setTextColor(getResources().getColor(R.color.black));

            // Khi nhấn vào button, nội dung sẽ đổ vào ô nhập tin nhắn
            button.setOnClickListener(v -> inputMessage.setText(suggestion));
// Tạo LayoutParams để thêm khoảng cách giữa các button
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(5, 10, 10, 0); // Thêm margin 10dp giữa các button
            button.setLayoutParams(params);
            // Thêm button vào layout
            suggestionLayout.addView(button);
        }
        for (String suggestion2 : suggestions2) {
            Button button = new Button(this);
            button.setText(suggestion2);
            button.setAllCaps(false);
            button.setPadding(16, 8, 16, 8);
            button.setBackgroundResource(R.drawable.bg_edittext_rounded);
            button.setTextColor(getResources().getColor(R.color.black));

            // Khi nhấn vào button, nội dung sẽ đổ vào ô nhập tin nhắn
            button.setOnClickListener(v -> inputMessage.setText(suggestion2));
// Tạo LayoutParams để thêm khoảng cách giữa các button
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(5, 0, 10, 0); // Thêm margin 10dp giữa các button
            button.setLayoutParams(params);
            // Thêm button vào layout
            suggestionLayout2.addView(button);
        }
    }
    ////tichhopchatgpt01
//    private void fetchChatbotResponse(String userMessage) {
//        List<ChatRequest.Message> messages = new ArrayList<>();
//        messages.add(new ChatRequest.Message("user", userMessage));
//
//        ChatRequest request = new ChatRequest("gpt-3.5-turbo", messages);
//
//        RetrofitClient.getApiService().getChatResponse(request).enqueue(new Callback<ChatResponse>() {
//            @Override
//            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
//                Log.d("ChatbotAPI", "Response Code: " + response.code());
//                if (response.isSuccessful() && response.body() != null) {
//                    String botResponse = response.body().getChoices().get(0).getMessage().getContent();
//                    Log.d("ChatbotAPI", "Bot Response: " + botResponse);
//                    chatMessages.add(new ChatMessage(botResponse, false));
//                    chatAdapter.notifyDataSetChanged();
//                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
//                } else {
//                    Log.e("ChatbotAPI", "Lỗi phản hồi từ AI! " + response.errorBody());
//                    Toast.makeText(ChatbotActivity.this, "Lỗi phản hồi từ AI!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ChatResponse> call, Throwable t) {
//                Log.e("ChatbotAPI", "Lỗi mạng: " + t.getMessage());
//                Toast.makeText(ChatbotActivity.this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
