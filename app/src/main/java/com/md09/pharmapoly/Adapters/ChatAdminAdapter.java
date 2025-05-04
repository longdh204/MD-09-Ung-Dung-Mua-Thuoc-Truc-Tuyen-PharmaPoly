package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.md09.pharmapoly.Models.ChatMessage;
import com.md09.pharmapoly.Models.Message;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.List;

public class ChatAdminAdapter extends RecyclerView.Adapter<ChatAdminAdapter.ViewHolder> {
    private final String user_id;
    private static final int TYPE_USER = 1;
    private static final int TYPE_BOT = 2;
    private Context context;
    private List<Message> chatMessages;

    public ChatAdminAdapter(Context context, String user_id, List<Message> chatMessages) {
        this.context = context;
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

        if (getItemViewType(position) != TYPE_USER) {
            User user = new SharedPrefHelper(context).getUser();
            if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
                Glide.with(context)
                        .load(user.getAvatar_url())
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(holder.img_avatar);
            }
        } else {
            holder.img_avatar.setImageResource(R.drawable.default_avatar);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView img_avatar;
        ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message);
            img_avatar = itemView.findViewById(R.id.img_avatar);
        }
    }

    public void addMessage(Message message) {
        chatMessages.add(message);
        notifyItemInserted(chatMessages.size() - 1);
    }
}