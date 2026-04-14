package com.wiredlogic.pichatclient.service;

import com.wiredlogic.pichatclient.model.ChatMessage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ChatServerClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ChatMessage> getMessages(String serverUrl) {
        try {
            ResponseEntity<List<ChatMessage>> response = restTemplate.exchange(
                    normalizeServerUrl(serverUrl) + "/messages",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ChatMessage>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (RestClientException e) {
            return Collections.emptyList();
        }
    }

    public String sendMessage(String serverUrl, String senderName, String content) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of(
                    "senderName", senderName,
                    "content", content
            );

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    normalizeServerUrl(serverUrl) + "/send",
                    HttpMethod.POST,
                    request,
                    Void.class
            );

            return "Message sent. Server responded with HTTP " + response.getStatusCode().value();
        } catch (RestClientException e) {
            return "Failed to send message: " + e.getMessage();
        }
    }

    private String normalizeServerUrl(String serverUrl) {
        if (serverUrl == null) {
            return "";
        }
        return serverUrl.trim().replaceAll("/+$", "");
    }
}