package com.md09.pharmapoly.converter;

import com.md09.pharmapoly.Models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MessageParser {
    public static List<Message> parseMessages(String jsonString) {
        List<Message> messageList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String message = jsonObject.getString("message");
                String senderId = jsonObject.getString("senderId");
                String timestamp = jsonObject.getString("timestamp");

                messageList.add(new Message(message, senderId, timestamp));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageList;
    }
}

