package com.example.newchatchat.Models;

import java.util.Date;

public class MessageModel {
    String uId,message,messageID;
    long timestamp;
    Date newTime;

    public MessageModel(String uId, String message, long timestamp, Date newTime) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
        this.newTime=newTime;
    }
    public MessageModel() {}

    public String getuId() {
        return uId;
    }

    public String getMessageID() {
        return messageID;
    }

    public Date getNewTime() {
        return newTime;
    }

    public void setNewTime(Date newTime) {
        this.newTime = newTime;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }
}
