package com.example.ayoub.nicper.Object.Message;

/**
 * Created by Admin on 23/06/2016.
 */
public class LastMessage {

    private String message;
    private String userId;
    private String username;
    private String otherUserId;
    private String otherUsername;

    public LastMessage() {
    }

    public LastMessage(String message, String userId, String username, String otherUserId, String otherUsername) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.otherUserId = otherUserId;
        this.otherUsername = otherUsername;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getMessage() {
        return message;
    }
}

