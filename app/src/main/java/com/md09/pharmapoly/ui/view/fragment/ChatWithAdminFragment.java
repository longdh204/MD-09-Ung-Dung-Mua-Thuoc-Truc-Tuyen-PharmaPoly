package com.md09.pharmapoly.ui.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private LinearLayout chatLayout;  // Thêm vào

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
        Log.d("HomeFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_chat_with_admin, container, false);
        rvChatAdmin = view.findViewById(R.id.rvChatAdmin);
        btnSendMessage = view.findViewById(R.id.btnSend);
        edTextMessage = view.findViewById(R.id.edtMessage);
        chatLayout = view.findViewById(R.id.chatLayout);  // Khởi tạo
        // Cài đặt listener để ẩn bàn phím khi ấn ra ngoài EditText
        view.setOnTouchListener((v, event) -> {
            // Kiểm tra nếu người dùng ấn vào bất kỳ vị trí nào ngoài EditText
            if (getActivity() != null && edTextMessage.isFocused()) {
                // Ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edTextMessage.getWindowToken(), 0);
                // Bỏ focus khỏi EditText
                edTextMessage.clearFocus();
            }
            return false;
        });

        btnSendMessage.setOnClickListener(view1 -> {
            String message = edTextMessage.getText().toString();
            if (message.length() == 0) {
                Toast.makeText(getContext(), "Vui lòng nhập tin nhắn", Toast.LENGTH_LONG).show();
                return;
            }
            socketManager.sendMessage("67b344c3744eaa2ff0f0ce7d", message);
            edTextMessage.setText("");  // Sau khi gửi tin nhắn, làm sạch EditText
        });

        int defaultMarginBottom = getResources().getDimensionPixelSize(R.dimen.default_margin_bottom);

        edTextMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Khi bắt đầu nhập, thay đổi marginBottom thành 0dp
                chatLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ) {{
                    bottomMargin = 0; // Đặt bottomMargin về 0 khi bắt đầu nhập
                }});
            } else {
                // Khi thoát khỏi phần nhập, quay lại marginBottom như cũ
                chatLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ) {{
                    bottomMargin = defaultMarginBottom; // Khôi phục giá trị cũ
                }});
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
