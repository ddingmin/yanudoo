package com.example.forhackerton.service;

import com.example.forhackerton.data.AskWithoutImgDto;
import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.QuestionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithoutImgService {

    private final MyChatGptService chatGptService;

    private final String preq = "해당 주제와 키워드에 해당하는 문제 짧게 만들어줘. 답도 출력해줘. " +
            "문제 : 답 형식으로.";

    public String preQuestion;

    @Autowired
    public WithoutImgService(MyChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    public ChatGptResponseDto getQuestion(AskWithoutImgDto askWithoutImgDto, Boolean flag){
        String subject = askWithoutImgDto.getSubject();
        String keyword = askWithoutImgDto.getKeyWord();
        QuestionRequestDto requestDto = new QuestionRequestDto();
        if(!flag){
            String question = preq + "주제 : " + subject + ", 키워드 : " + keyword;
            requestDto.setQuestion(question);
            ChatGptResponseDto responseDto = chatGptService.askQuestion(requestDto);
            return responseDto;
        }else{
            String question = preq + "주제 : " + subject + ", 키워드 : " + keyword + ", " + preQuestion + "과 다른 문제 생성해줘.";
            requestDto.setQuestion(question);
            ChatGptResponseDto responseDto = chatGptService.askQuestion(requestDto);
            return responseDto;
        }
    }

    public ChatGptResponseDto getWithPreQuestion(AskWithoutImgDto askWithoutImgDto, String preQuestion){
        String subject = askWithoutImgDto.getSubject();
        String keyword = askWithoutImgDto.getKeyWord();
        QuestionRequestDto requestDto = new QuestionRequestDto();
        String question = preq + "주제 : " + subject + ", 키워드 : " + keyword + ", " + preQuestion + "과 다른 문제 생성해줘.";
        requestDto.setQuestion(question);
        ChatGptResponseDto responseDto = chatGptService.askQuestion(requestDto);
        return responseDto;
    }
}
