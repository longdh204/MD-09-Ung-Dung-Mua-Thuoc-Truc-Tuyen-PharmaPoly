package com.md09.pharmapoly.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.ChatMessage;
import com.md09.pharmapoly.Models.Message;
import com.md09.pharmapoly.R;

import java.util.List;

public class ChatAdminAdapter extends RecyclerView.Adapter<ChatAdminAdapter.ViewHolder> {
    private final String user_id ;
    private static final int TYPE_USER = 1;
    private static final int TYPE_BOT = 2;

    private List<Message> chatMessages;

    public ChatAdminAdapter(String user_id, List<Message> chatMessages) {
        this.chatMessages = chatMessages;
        this.user_id = user_id;
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).getSenderId().equals(user_id) ? TYPE_USER : TYPE_BOT;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_admin, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = chatMessages.get(position);
        holder.messageText.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message);
        }
    }

    public void addMessage(Message message) {
        chatMessages.add(message);
        notifyItemInserted(chatMessages.size() - 1);
    }
}
