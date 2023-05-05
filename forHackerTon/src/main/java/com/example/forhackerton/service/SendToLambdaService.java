package com.example.forhackerton.service;

import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.QuestionRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class SendToLambdaService {

    private static final String endPoint = "https://nbv3uazcmg.execute-api.ap-southeast-2.amazonaws.com/default/forHackerTon";

    private final MyChatGptService chatGptService;

    private static final String preq = "다음 문장과 비슷한 문제 짧게 생성해줘";

    Logger logger = LoggerFactory.getLogger(SendToLambdaService.class);

    @Autowired
    public SendToLambdaService(MyChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    /*
    이미지 파일 -> Lambda에 전송, 비슷한 유형의 문제 만들어주는 API
     */
    public ChatGptResponseDto sendByteWithLambda(byte[] bytes){
        try{
            String base64Encoded = Base64.getEncoder().encodeToString(bytes);
            org.springframework.http.HttpHeaders headers = new HttpHeaders();
            //에러가 날 가능성 높음
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(bytes.length);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>(base64Encoded, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(endPoint, entity, String.class);

            String responseBody = response.getBody();

            QuestionRequestDto requestDto = new QuestionRequestDto();
            requestDto.setQuestion(preq + "\n" + responseBody);

            ChatGptResponseDto responseDto = chatGptService.askQuestion(requestDto);

            return responseDto;
        }catch (Exception e){
            e.printStackTrace();
            return new ChatGptResponseDto();
        }
    }
}
