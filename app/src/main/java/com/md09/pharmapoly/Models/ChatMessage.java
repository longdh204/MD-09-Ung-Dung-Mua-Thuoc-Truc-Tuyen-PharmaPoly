package com.md09.pharmapoly.Models;

public class ChatMessage {
    private String message;
    private boolean isUser;

    public ChatMessage() {}

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() { return message; }
    public boolean isUser() { return isUser; }
}
