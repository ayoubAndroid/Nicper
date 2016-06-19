package com.example.ayoub.nicper.Object.Message;

/**
 * Created by Admin on 04/06/2016.
 */
public class Message {
    private Time time;
    private String message;

    public Message(Time time, String message) {
        this.time = time;
        this.message = message;
    }

    public Time getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
