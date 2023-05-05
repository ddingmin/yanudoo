package com.example.forhackerton.service;

import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.QuestionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
많이 사용하는 Clova 결과 Response Service
 */
@Service
public class CommonService {

    private MyChatGptService chatGptService;

    @Autowired
    public CommonService(MyChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    public ChatGptResponseDto getCommonResponse(String answer, String preq){
        //answer : clova 결과!
        QuestionRequestDto requestDto = new QuestionRequestDto();
        requestDto.setQuestion(answer); //Clova 결과 데이터
        ChatGptResponseDto responseDto = chatGptService.askWithPreQuestion(preq, requestDto);

        return responseDto;
    }


}
