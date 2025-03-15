package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.md09.pharmapoly.Adapters.ChatAdapter;
import com.md09.pharmapoly.Models.ChatMessage;
import com.md09.pharmapoly.Models.ChatRequest;
import com.md09.pharmapoly.Models.ChatResponse;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText inputMessage;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> sendMessage());
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
    DatabaseReference chatbotRef = FirebaseDatabase.getInstance().getReference("chatbot");
    private void fetchChatbotResponse(String userMessage) {
        DatabaseReference chatbotRef = FirebaseDatabase.getInstance().getReference("chatbot");

        chatbotRef.child(userMessage.toLowerCase()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String botResponse = snapshot.getValue(String.class);
                    chatMessages.add(new ChatMessage(botResponse, false));
                } else {
                    chatMessages.add(new ChatMessage("Xin lỗi, tôi chưa hiểu câu hỏi này!", false));
                }
            } else {
                chatMessages.add(new ChatMessage("Lỗi kết nối đến chatbot!", false));
            }
            chatAdapter.notifyDataSetChanged();
        });
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
