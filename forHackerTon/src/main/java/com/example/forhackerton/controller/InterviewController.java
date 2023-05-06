package com.example.forhackerton.controller;


import com.example.forhackerton.common.BaseResponse;
import com.example.forhackerton.config.RegularResponseStatus;
import com.example.forhackerton.data.QuestionRequestDto;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/Interview")
public class InterviewController {

    @org.springframework.beans.factory.annotation.Value("${openai.api-key}")
    private String token;

    private final List<ChatMessage> messages = new ArrayList<>();
    Logger logger = LoggerFactory.getLogger(InterviewController.class);

    @ResponseBody
    @PostMapping("/makeChat")
    public BaseResponse chatWithMe(@RequestBody QuestionRequestDto requestDto){

        OpenAiService service = new OpenAiService(token);
        ChatMessage systemMessage = new ChatMessage(
                ChatMessageRole.SYSTEM.value(), "면접봇");
        String k = requestDto.getQuestion();
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), k));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
//                .n(1)
                .maxTokens(700)
                .temperature(0.0)
//                .logitBias(new HashMap<>())
                .build();
        String answer = service.createChatCompletion(chatCompletionRequest).getChoices().get(0)
                .getMessage().getContent();
        logger.info(answer);
        messages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), answer));

        return new BaseResponse(RegularResponseStatus.OK.getCode(), answer, RegularResponseStatus.OK.getMessage());
    }

}
