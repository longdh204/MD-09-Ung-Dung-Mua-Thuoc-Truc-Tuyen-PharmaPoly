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
import com.md09.pharmapoly.ui.view.activity.MainActivity;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.List;

import androidx.activity.OnBackPressedCallback;

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
    public void onResume() {
        super.onResume();
        // Reset trạng thái của bottom navigation
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Ẩn bàn phím khi fragment bị tạm dừng
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edTextMessage.getWindowToken(), 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_chat_with_admin, container, false);
        rvChatAdmin = view.findViewById(R.id.rvChatAdmin);
        btnSendMessage = view.findViewById(R.id.btnSend);
        edTextMessage = view.findViewById(R.id.edtMessage);
        chatLayout = view.findViewById(R.id.chatLayout);
        int defaultMarginBottom = getResources().getDimensionPixelSize(R.dimen.default_margin_bottom);

        // Thêm listener cho EditText
        edTextMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                chatLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ) {{
                    bottomMargin = 0;
                }});
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setBottomNavigationVisibility(false);
                }
                // Hiển thị bàn phím
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edTextMessage, InputMethodManager.SHOW_FORCED);
                }
            } else {
                chatLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ) {{
                    bottomMargin = defaultMarginBottom;
                }});
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
                }
            }
        });

        // Thêm listener cho toàn bộ view để xử lý khi click ra ngoài
        view.setOnClickListener(v -> {
            if (edTextMessage.isFocused()) {
                edTextMessage.clearFocus();
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edTextMessage.getWindowToken(), 0);
                }
            }
        });

        // Thêm listener cho RecyclerView
        rvChatAdmin.setOnClickListener(v -> {
            if (edTextMessage.isFocused()) {
                edTextMessage.clearFocus();
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edTextMessage.getWindowToken(), 0);
                }
            }
        });

        // Thêm OnTouchListener cho RecyclerView
        rvChatAdmin.setOnTouchListener((v, event) -> {
            if (edTextMessage.isFocused()) {
                edTextMessage.clearFocus();
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edTextMessage.getWindowToken(), 0);
                }
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
            edTextMessage.setText("");
            if (getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edTextMessage.getWindowToken(), 0);
                edTextMessage.clearFocus();
                chatLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ) {{
                    bottomMargin = defaultMarginBottom;
                }});
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
                }
            }
        });

        // Xử lý nút back
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (edTextMessage.isFocused()) {
                    edTextMessage.clearFocus();
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edTextMessage.getWindowToken(), 0);
                    chatLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    ) {{
                        bottomMargin = defaultMarginBottom;
                    }});
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
                    }
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
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
            ChatAdminAdapter chatAdminAdapter = new ChatAdminAdapter(getContext(), userId, messageList);
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
