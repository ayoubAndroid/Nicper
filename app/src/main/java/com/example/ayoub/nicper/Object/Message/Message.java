package com.example.ayoub.nicper.Object.Message;

/**
 * Created by Admin on 04/06/2016.
 */
public class Message {

    private String message;
    private String userId;
    private String username;
    private Time time;

    public Message() {
    }

    public Message(String message, String userId, String username, Time time) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.time = time;
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

    public String getMessage() {
        return message;
    }
    public Time getTime(){
        return time;
    }
}
