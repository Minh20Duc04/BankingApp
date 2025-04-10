package com.BankingApplication.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotRequest {

    private String model;
    private List<Message> messages;

    public ChatBotRequest(String model, String prompt) {
        this.model = model;
        this.messages = List.of(new Message("user", prompt));
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
