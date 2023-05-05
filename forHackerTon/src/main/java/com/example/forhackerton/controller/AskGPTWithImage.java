package com.example.forhackerton.controller;
/*
해당 문제가 있는 경우, 파일을 바로 Lambda에 파싱하는 API
 */

import com.example.forhackerton.common.BaseResponse;
import com.example.forhackerton.config.RegularResponseStatus;
import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.QuestionRequestDto;
import com.example.forhackerton.service.ClovaService;
import com.example.forhackerton.service.MyChatGptService;
import com.example.forhackerton.service.SendToLambdaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ImageSend")
public class AskGPTWithImage {

    Logger logger = LoggerFactory.getLogger(AskGPTWithImage.class);

    private SendToLambdaService sendToLambdaService;
    private ClovaService clovaService;

    private MyChatGptService chatGptService;

    private final String preq = "비슷한 유형의 문제 만들고, 답변 출력해줘. 형식은 문제 : , 답 : 이런식으로 ";

    @Autowired
    public AskGPTWithImage(SendToLambdaService sendToLambdaService, ClovaService clovaService, MyChatGptService chatGptService) {
        this.sendToLambdaService = sendToLambdaService;
        this.clovaService = clovaService;
        this.chatGptService = chatGptService;
    }


    /*
    Lambda 테스트 코드 -> 사용 X
     */
    @ResponseBody
    @PostMapping("/askWithImage")
    public BaseResponse askWithImage(@RequestParam("file")MultipartFile file) throws IOException {
        long startTime = System.currentTimeMillis();
        try{
            byte[] bytes = file.getBytes();
            ChatGptResponseDto responseDto = sendToLambdaService.sendByteWithLambda(bytes);
            logger.info("service successfully transfered");
            logger.info("사진 데이터 Parsing");
            long endTime = System.currentTimeMillis();
            logger.info("총시간 : " + (endTime - startTime)/ 1000 + "." +  (endTime - startTime)%1000 + "초");
            return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto, RegularResponseStatus.OK.getMessage());
        }catch (Exception e){
            logger.info("Image data Parsing Error!!");
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }

    /*
    문제 사진이 있는 경우, 비슷한 유형의 문제 / 답 알려주는 API
     */
    @ResponseBody
    @PostMapping("/detect")
    public BaseResponse askColvaImage(@RequestParam("file")MultipartFile file) throws IOException{
        long startTime = System.currentTimeMillis();
        try{

            String answer = clovaService.getClovaResponse(file);
            if(answer.isEmpty()){
                return new BaseResponse<>(RegularResponseStatus.INTERNAL_SERVER_ERROR.getCode(), "ERROR", RegularResponseStatus.INTERNAL_SERVER_ERROR.getMessage());
            }


            QuestionRequestDto requestDto = new QuestionRequestDto();
            logger.info("1");
            requestDto.setQuestion(answer);
            logger.info("2");
            ChatGptResponseDto responseDto = chatGptService.askSimmilarQuestion(requestDto);
            logger.info("3");
            logger.info(responseDto.toString());
            long endTime = System.currentTimeMillis();
            logger.info("총시간 : " + (endTime - startTime)/ 1000 + "." +  (endTime - startTime)%1000 + "초");
            return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto, RegularResponseStatus.OK.getMessage());
        }catch (Exception e){
            logger.info("Image Processing ERROR!");
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }

    /*
    문제 정답 출력 API
     */
    @ResponseBody
    @PostMapping("/justAnswer")
    public BaseResponse askWithImg(@RequestParam("file")MultipartFile file) throws IOException{
        long startTime = System.currentTimeMillis();
        try{
            String answer = clovaService.getClovaResponse(file);
            if(answer.isEmpty()){
                return new BaseResponse<>(RegularResponseStatus.INTERNAL_SERVER_ERROR.getCode(), "ERROR", RegularResponseStatus.INTERNAL_SERVER_ERROR.getMessage());
            }
            QuestionRequestDto requestDto = new QuestionRequestDto();
            logger.info("1");
            requestDto.setQuestion(answer);
            logger.info("2");
            ChatGptResponseDto responseDto = chatGptService.askAnswerInQuestion(requestDto);
            logger.info("3");
            logger.info(responseDto.getChoices().toString());
            long endTime = System.currentTimeMillis();
            QuestionRequestDto requestDto1 = new QuestionRequestDto();
            requestDto1.setQuestion(responseDto.getChoices().toString());
            ChatGptResponseDto responseDto1 = chatGptService.askAnswerInQuestion(requestDto1);
            return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto1, RegularResponseStatus.OK.getMessage());
        }catch (Exception e){
            logger.info("image Processing Error! with checking Wright Answer");
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }
}
