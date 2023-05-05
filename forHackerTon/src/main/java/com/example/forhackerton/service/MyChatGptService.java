package com.example.forhackerton.service;

import com.example.forhackerton.config.ChatGptConfig;
import com.example.forhackerton.data.ChatGptRequestDto;
import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.QuestionRequestDto;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyChatGptService {

    Logger logger = LoggerFactory.getLogger(MyChatGptService.class);

    private final RestTemplate restTemplate;

    private final ChatgptService chatgptService;

    @org.springframework.beans.factory.annotation.Value("${openai.api-key}")
    private String apiKey;

    @Value("${chatgpt.end-point}")
    private String URL;

    public MyChatGptService(RestTemplate restTemplate, ChatgptService chatgptService) {
        this.restTemplate = restTemplate;
        this.chatgptService = chatgptService;
    }public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
//        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class);

        return responseEntity.getBody();
    }

    public ChatGptResponseDto askQuestion(QuestionRequestDto requestDto) {
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGptConfig.MODEL,
                                requestDto.getQuestion(),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
    }

    public ChatGptResponseDto askSimmilarQuestion(QuestionRequestDto requestDto){
        final String question = "비슷한 유형의 문제 만들고, 답변 출력해줘. 형식은 문제 : , 답 : 이런식으로 ";

        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGptConfig.MODEL,
                                question + requestDto.getQuestion(),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
    }

}