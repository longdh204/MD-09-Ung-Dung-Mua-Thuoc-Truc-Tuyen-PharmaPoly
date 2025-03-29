package com.md09.pharmapoly.ui.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.md09.pharmapoly.Adapters.ChatAdminAdapter;
import com.md09.pharmapoly.Models.Message;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.network.SocketManager;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.List;

public class ChatWithAdminFragment extends Fragment implements SocketManager.UpdateChatFragment {
    private SocketManager socketManager;
    private RecyclerView rvChatAdmin;
    private EditText edTextMessage;
    private Button btnSendMessage;

    public ChatWithAdminFragment() {
    }

    public static ChatWithAdminFragment newInstance() {
        return new ChatWithAdminFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_with_admin, container, false);
        rvChatAdmin = view.findViewById(R.id.rvChatAdmin);
        btnSendMessage = view.findViewById(R.id.btnSend);
        edTextMessage = view.findViewById(R.id.edtMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edTextMessage.getText().toString();
                if (message.length() == 0) {
                    Toast.makeText(getContext(), "Vui lòng nhập tin nhắn", Toast.LENGTH_LONG).show();
                    return;
                }
                socketManager.sendMessage("W7LRUTJQUEVBBthDw80GYhPK07E2", message);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userId = new SharedPrefHelper(requireContext()).getUser().get_id();
        socketManager = SocketManager.getInstance(userId);
        socketManager.setUpdateChatFragment(this);
        socketManager.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socketManager.disconnect();
    }

    @Override
    public void showOldMessage(List<Message> messageList) {
        requireActivity().runOnUiThread(() -> {
            String userId = new SharedPrefHelper(requireContext()).getUser().get_id();
            ChatAdminAdapter chatAdminAdapter = new ChatAdminAdapter(userId, messageList);
            rvChatAdmin.setAdapter(chatAdminAdapter);
            rvChatAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
            rvChatAdmin.scrollToPosition(rvChatAdmin.getAdapter().getItemCount() - 1);

        });
    }

    @Override
    public void updateMessage(Message message) {
        requireActivity().runOnUiThread(() -> {
            if (rvChatAdmin.getAdapter() instanceof ChatAdminAdapter) {
                ((ChatAdminAdapter) rvChatAdmin.getAdapter()).addMessage(message);
                rvChatAdmin.scrollToPosition(rvChatAdmin.getAdapter().getItemCount() - 1);
                edTextMessage.setText("");
            }
        });
    }
}