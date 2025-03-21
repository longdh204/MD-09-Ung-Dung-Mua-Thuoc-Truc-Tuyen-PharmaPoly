package com.md09.pharmapoly.data.model;

import java.util.List;

public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private String token;
    private String refreshToken;

    public ApiResponse() {
    }

    public ApiResponse(int status, String message, T data, String token, String refreshToken) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
