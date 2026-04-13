package com.wiredlogic.pichatclient.controller;

import com.wiredlogic.pichatclient.model.ChatMessage;
import com.wiredlogic.pichatclient.model.MessageForm;
import com.wiredlogic.pichatclient.service.ChatServerClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Controller
public class ChatPageController {

    private final ChatServerClient chatServerClient;

    public ChatPageController(ChatServerClient chatServerClient) {
        this.chatServerClient = chatServerClient;
    }

    @GetMapping("/")
    public String showChatPage(
            @RequestParam(name = "serverUrl", required = false, defaultValue = "http://localhost:45123") String serverUrl,
            Model model
    ) {
        MessageForm form = new MessageForm();
        form.setServerUrl(serverUrl);

        List<ChatMessage> messages = chatServerClient.getMessages(serverUrl);

        model.addAttribute("messageForm", form);
        model.addAttribute("messages", messages);
        model.addAttribute("statusMessage", "");
        model.addAttribute("clientIp", getLocalIpAddress());

        return "chat";
    }

    @PostMapping("/send")
    public String sendMessage(@ModelAttribute MessageForm messageForm, Model model) {
        String statusMessage;

        if (messageForm.getServerUrl() == null || messageForm.getServerUrl().isBlank()) {
            statusMessage = "Server URL is required.";
        } else if (messageForm.getContent() == null || messageForm.getContent().isBlank()) {
            statusMessage = "Message content is required.";
        } else {
            statusMessage = chatServerClient.sendMessage(
                    messageForm.getServerUrl(),
                    messageForm.getContent().trim()
            );
        }

        List<ChatMessage> messages = chatServerClient.getMessages(messageForm.getServerUrl());

        MessageForm freshForm = new MessageForm();
        freshForm.setServerUrl(messageForm.getServerUrl());

        model.addAttribute("messageForm", freshForm);
        model.addAttribute("messages", messages);
        model.addAttribute("statusMessage", statusMessage);
        model.addAttribute("clientIp", getLocalIpAddress());

        return "chat";
    }

    @PostMapping("/refresh")
    public String refreshMessages(@ModelAttribute MessageForm messageForm, Model model) {
        List<ChatMessage> messages = chatServerClient.getMessages(messageForm.getServerUrl());

        model.addAttribute("messageForm", messageForm);
        model.addAttribute("messages", messages);
        model.addAttribute("statusMessage", "Messages refreshed.");
        model.addAttribute("clientIp", getLocalIpAddress());

        return "chat";
    }

    private String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}