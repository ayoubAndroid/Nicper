package com.example.ayoub.nicper.Object.Message;

/**
 * Created by Admin on 04/06/2016.
 */
public class Message {
    private String message;
    private String userId;

    public Message() {
    }

    public Message(String message, String userId) {
        this.message = message;
        this.userId = userId;

    }

    public String getUserId() {
        return userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }
}
