package com.vibezz.Models;

public class Message {

    private String messageId;
    private String message;
    private String senderId;
    private long timestamp;

    public Message(String messageText, String senderUId, long time) {
    }

    public Message(String messageId, String message) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

}
