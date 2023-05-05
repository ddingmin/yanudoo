package com.example.forhackerton.controller;

import com.example.forhackerton.common.BaseResponse;
import com.example.forhackerton.config.RegularResponseStatus;
import com.example.forhackerton.data.AskWithoutImgDto;
import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.QuestionRequestDto;
import com.example.forhackerton.service.MyChatGptService;
import com.example.forhackerton.service.WithoutImgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/KeyWord")
public class AskGPTWithoutImage {

    Logger logger = LoggerFactory.getLogger(AskGPTWithoutImage.class);

    private final String preq = "해당 주제와 키워드에 해당하는 문제 짧게 만들어줘. 답도 출력해줘. " +
            "문제 : 답 형식으로.";

    private WithoutImgService withoutImgService;

    private Boolean flag = false;

    public String preQuestion;

    @Autowired
    public AskGPTWithoutImage(WithoutImgService withoutImgService) {
        this.withoutImgService = withoutImgService;
    }

    /*
        문제 사진이 없는 경우, 주제 + 키워드로 문제를 생성해주는 API
        호출시마다 문제 바뀜
         */
    @ResponseBody
    @PostMapping("/noImg")
    public BaseResponse askWithoutImage(@RequestBody AskWithoutImgDto askWithoutImgDto){
        try{
            String subject = askWithoutImgDto.getSubject();
            String keyword = askWithoutImgDto.getKeyWord();
            QuestionRequestDto requestDto = new QuestionRequestDto();
            if(!flag){
                //첫 질문
                flag = true; //다음 질문시 새로운 질문을 받을 수 있도록
                ChatGptResponseDto responseDto = withoutImgService.getQuestion(askWithoutImgDto, flag);
                return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto, RegularResponseStatus.OK.getMessage());
            }else{
                ChatGptResponseDto responseDto = withoutImgService.getWithPreQuestion(askWithoutImgDto, preQuestion);
                preQuestion = responseDto.getChoices().toString();
                return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto, RegularResponseStatus.OK.getMessage());
            }

        }catch (Exception e){
            logger.info("noImgError!");
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }
}
