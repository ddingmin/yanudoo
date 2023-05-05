package com.example.forhackerton.controller;

import com.example.forhackerton.service.MyChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GptController {

    private final MyChatGptService chatGptService;

    @Autowired
    public GptController(MyChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }


}
