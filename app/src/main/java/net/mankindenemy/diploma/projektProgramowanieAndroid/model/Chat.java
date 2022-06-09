package net.mankindenemy.diploma.projektProgramowanieAndroid.model;

import com.google.firebase.database.ServerValue;

public class Chat {

    private String sender, receiver, message, key;
    private Object time;


    public Chat() {

    }

    public Chat(String sender, String receiver, String message) {

        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = ServerValue.TIMESTAMP;
    }

    public String getSender() {

        return sender;
    }

    public void setSender(String sender) {

        this.sender = sender;
    }

    public String getReceiver() {

        return receiver;
    }

    public void setReceiver(String receiver) {

        this.receiver = receiver;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public Object getTime() {

        return time;
    }

    public void setTime(Object time) {

        this.time = time;
    }

}
