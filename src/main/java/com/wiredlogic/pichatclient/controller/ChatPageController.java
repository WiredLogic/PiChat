package com.wiredlogic.pichatclient.controller;

import com.wiredlogic.pichatclient.model.ChatMessage;
import com.wiredlogic.pichatclient.model.MessageForm;
import com.wiredlogic.pichatclient.service.ChatServerClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "senderName", required = false, defaultValue = "anon") String senderName,
            Model model
    ) {
        MessageForm form = new MessageForm();
        form.setServerUrl(serverUrl);
        form.setSenderName(senderName);

        List<ChatMessage> messages = chatServerClient.getMessages(serverUrl);

        model.addAttribute("messageForm", form);
        model.addAttribute("messages", messages);
        model.addAttribute("statusMessage", "");
        model.addAttribute("currentSenderName", senderName);

        return "chat";
    }

    @PostMapping("/send")
    public String sendMessage(@ModelAttribute MessageForm messageForm, Model model) {
        String statusMessage;

        if (messageForm.getServerUrl() == null || messageForm.getServerUrl().isBlank()) {
            statusMessage = "Server URL is required.";
        } else if (messageForm.getSenderName() == null || messageForm.getSenderName().isBlank()) {
            statusMessage = "Sender name is required.";
        } else if (messageForm.getContent() == null || messageForm.getContent().isBlank()) {
            statusMessage = "Message content is required.";
        } else {
            statusMessage = chatServerClient.sendMessage(
                    messageForm.getServerUrl(),
                    messageForm.getSenderName().trim(),
                    messageForm.getContent().trim()
            );
        }

        List<ChatMessage> messages = chatServerClient.getMessages(messageForm.getServerUrl());

        MessageForm freshForm = new MessageForm();
        freshForm.setServerUrl(messageForm.getServerUrl());
        freshForm.setSenderName(messageForm.getSenderName());

        model.addAttribute("messageForm", freshForm);
        model.addAttribute("messages", messages);
        model.addAttribute("statusMessage", statusMessage);
        model.addAttribute("currentSenderName", messageForm.getSenderName());

        return "chat";
    }

    @PostMapping("/refresh")
    public String refreshMessages(@ModelAttribute MessageForm messageForm, Model model) {
        List<ChatMessage> messages = chatServerClient.getMessages(messageForm.getServerUrl());

        model.addAttribute("messageForm", messageForm);
        model.addAttribute("messages", messages);
        model.addAttribute("statusMessage", "Messages refreshed.");
        model.addAttribute("currentSenderName", messageForm.getSenderName());

        return "chat";
    }
}