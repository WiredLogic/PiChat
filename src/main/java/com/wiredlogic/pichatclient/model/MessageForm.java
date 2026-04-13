package com.wiredlogic.pichatclient.model;

public class MessageForm {
    private String serverUrl;
    private String content;

    public MessageForm() {
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}