package com.BankingApplication.Controller;

import com.BankingApplication.Dto.ChatBotRequest;
import com.BankingApplication.Dto.ChatBotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/bot")
public class ChatBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    @Qualifier("openAiRestTemplate")
    private RestTemplate template;


    @PostMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt){ //mỗi lần chạy phải thay đổi api key trong yml và đổi api key trong Authorization trong header
        ChatBotRequest request = new ChatBotRequest(model, prompt);
        ChatBotResponse chatBotResponse = template.postForObject(apiURL, request, ChatBotResponse.class);
        return chatBotResponse.getChoices().get(0).getMessage().getContent();
    }

}
