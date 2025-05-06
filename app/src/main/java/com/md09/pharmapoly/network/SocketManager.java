package com.md09.pharmapoly.network;

import static com.md09.pharmapoly.utils.Constants.BASE_URL;

import android.util.Log;

import com.md09.pharmapoly.Models.Message;
import com.md09.pharmapoly.converter.MessageParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private UpdateChatFragment updateChatFragment;
    private static final String SERVER_URL = "https://pharmapoly-server.onrender.com";
//    private static final String SERVER_URL = "http://192.168.147.223:3000";
    private static SocketManager instance;
    private Socket socket;
    private final String userId;

    private SocketManager(String userId) {
        this.userId = userId;
        try {
            IO.Options options = new IO.Options();
            options.query = "userId=" + userId;
            socket = IO.socket(SERVER_URL, options);
        } catch (URISyntaxException e) {
            Log.e("SocketIO", "Lỗi kết nối Socket.IO: " + e.getMessage());
        }
    }

    public static SocketManager getInstance(String userId) {
        if (instance == null) {
            instance = new SocketManager(userId);
        }
        return instance;
    }

    public void connect() {
        if (socket != null && !socket.connected()) {
            socket.connect();
            socket.on(Socket.EVENT_CONNECT, args ->
                    Log.d("SocketIO", "Kết nối thành công với userId: " + userId)
            );
            socket.on(Socket.EVENT_CONNECT_ERROR, args ->
                    Log.e("SocketIO", "Lỗi kết nối: " + args[0])
            );

            // 🔥 Nhận tin nhắn cũ từ server
            socket.on("oldMessages", args -> {
                List<Message> messageList = MessageParser.parseMessages(args[0].toString());
                updateChatFragment.showOldMessage(messageList);
            });

            // 🔥 Nhận phản hồi khi gửi tin nhắn thành công
            socket.on("messageSentSuccess", args -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    JSONObject lastMessage = data.getJSONObject("data");

                    Message newMessage = new Message(
                            lastMessage.getString("message"),
                            lastMessage.getString("senderId"),
                            lastMessage.getString("timestamp")
                    );

                    updateChatFragment.updateMessage(newMessage);
                } catch (JSONException e) {
                    Log.e("socket_io", Objects.requireNonNull(e.getMessage()));
                }
            });

            // 🔥 Nhận tin nhắn mới từ server
            socket.on("receiveMessage", args -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    JSONObject lastMessage = data.getJSONObject("data");

                    Message newMessage = new Message(
                            lastMessage.getString("message"),
                            lastMessage.getString("senderId"),
                            lastMessage.getString("timestamp")
                    );

                    updateChatFragment.updateMessage(newMessage);
                } catch (JSONException e) {
                    Log.e("socket_io", Objects.requireNonNull(e.getMessage()));
                }
            });

        }
    }

    public void disconnect() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
            Log.d("SocketIO", "Ngắt kết nối");
        }
    }

    public void sendMessage(String receiverId, String message) {
        Log.e("Check receiverId",receiverId);
        Log.e("Check message",message);
        if (socket != null && socket.connected()) {
            try {
                JSONObject data = new JSONObject();
                data.put("senderId", userId);
                data.put("receiverId", receiverId);
                data.put("message", message);
                socket.emit("sendMessage", data);
            } catch (JSONException e) {
                Log.e("SocketIO", "Lỗi gửi tin nhắn: " + e.getMessage());
            }
        } else {
            Log.e("SocketIO", "Socket chưa kết nối!");
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public interface UpdateChatFragment {
        void showOldMessage(List<Message> messageList);

        void updateMessage(Message message);
    }

    public void setUpdateChatFragment(UpdateChatFragment updateChatFragment) {
        this.updateChatFragment = updateChatFragment;
    }
}