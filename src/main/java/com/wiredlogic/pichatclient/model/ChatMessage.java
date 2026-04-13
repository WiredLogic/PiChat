package com.wiredlogic.pichatclient.model;

import java.time.LocalDateTime;

public class ChatMessage {

    private String content;
    private String senderIp;
    private LocalDateTime timestamp;

    public ChatMessage() {}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderIp() {
        return senderIp;
    }

    public void setSenderIp(String senderIp) {
        this.senderIp = senderIp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}