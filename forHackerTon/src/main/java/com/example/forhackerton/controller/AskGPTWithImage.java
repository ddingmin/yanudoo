package com.example.forhackerton.controller;
/*
해당 문제가 있는 경우, 파일을 바로 Lambda에 파싱하는 API
 */

import com.example.forhackerton.common.BaseResponse;
import com.example.forhackerton.config.RegularResponseStatus;
import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.QtoQSaveDto;
import com.example.forhackerton.data.QuestionRequestDto;
import com.example.forhackerton.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
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

    private CommonService commonService;

    private SqlService sqlService;

    private ChangeToString changeToString;

    public String beforeQuestion = "";

    public String beforeAnswer = "";

    @Autowired
    public AskGPTWithImage(SendToLambdaService sendToLambdaService, ClovaService clovaService,
                           MyChatGptService chatGptService, CommonService commonService,
                           SqlService sqlService, ChangeToString changeToString) {
        this.sendToLambdaService = sendToLambdaService;
        this.clovaService = clovaService;
        this.chatGptService = chatGptService;
        this.commonService = commonService;
        this.sqlService = sqlService;
        this.changeToString = changeToString;
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
        final String preq = "비슷한 문제 아주 새롭게 만들고, 답변 출력해줘. 형식은 문제 : , 답 : 이런식으로 ";
        try{
            String answer = clovaService.getClovaResponse(file);
            System.out.println("Available memory (MB): " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
            if(answer.isEmpty()){
                return new BaseResponse<>(RegularResponseStatus.INTERNAL_SERVER_ERROR.getCode(), "ERROR", RegularResponseStatus.INTERNAL_SERVER_ERROR.getMessage());
            }
            ChatGptResponseDto responseDto = commonService.getCommonResponse(answer, preq);
            System.out.println("Available memory (MB): " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
            long endTime = System.currentTimeMillis();

            QtoQSaveDto saveDto = new QtoQSaveDto();
            String result = responseDto.getChoices().get(0).getText().replaceFirst("^\\n\\n", "");
            saveDto.setOriginalQuestion(changeToString.makeBlank(changeToString.clovaToString(answer)).replaceFirst("^\\n\\n", ""));
            saveDto.setOriginalAnswer(result.replaceFirst("^\\n\\n", "")); //값 잘 들어가는지 찍어보기
            saveDto.setGeneratedQuestion(changeToString.makeBlank(changeToString.clovaToString(answer)).replaceFirst("^\\n\\n", ""));
            saveDto.setGeneratedAnswer(result.replaceFirst("^\\n\\n", ""));

            logger.info(result);
            sqlService.QtoQSave(saveDto);

            logger.info("qto question has saved");
            logger.info("총시간 : " + (endTime - startTime)/ 1000 + "." +  (endTime - startTime)%1000 + "초");
            System.out.println("Available memory (MB): " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
            return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto, RegularResponseStatus.OK.getMessage());
        }catch (Exception e){
            logger.info("Image Processing ERROR!");
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }

    /*
    문제 정답 출력 API
    ChatGPT를 두번 호출해, 문제 배열을 정렬시키고 해당하는 정답 출력함.
    DB저장 안함.
     */
    @ResponseBody
    @PostMapping("/justAnswer")
    public BaseResponse askWithImg(@RequestParam("file")MultipartFile file) throws IOException{
        System.out.println("Available memory (MB): " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
        final String preq = "아래 문제의 정답만 정확히 얘기해줘.";
        logger.info("2");
        System.out.println("2");
        try{
            String answer = clovaService.getClovaResponse(file);
            System.out.println("Available memory (MB): " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
            if(answer.isEmpty()){
                logger.info("clova ERROR!");
                return new BaseResponse<>(RegularResponseStatus.INTERNAL_SERVER_ERROR.getCode(), "ERROR", RegularResponseStatus.INTERNAL_SERVER_ERROR.getMessage());
            }
            ChatGptResponseDto responseDto = commonService.getCommonResponse(answer, preq);
//          //정확도를 위해 2번 Request 진행
            ChatGptResponseDto correctResponseDto = commonService.getCommonResponse(responseDto.getChoices().toString(), preq);
            System.out.println("Available memory (MB): " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
            return new BaseResponse<>(RegularResponseStatus.OK.getCode(), correctResponseDto.getChoices().get(0).getText().replaceFirst("^\\n\\n", ""), RegularResponseStatus.OK.getMessage());
        }catch (Exception e){
            logger.info("image Processing Error! with checking Wright Answer");
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }

    /*
    여러번 호출시 -> 서로 다른 문제 / 답 호출 가능
    DB저장 O, Concept -> 문제 -> 답안 호출.
    QuestionToQuestion 방식
    이건 makeBlank 없이도 값 잘 들어감.
     */
    @ResponseBody
    @PostMapping("/withConcept")
    public BaseResponse askWithConceptWithImg(@RequestParam("file")MultipartFile file) throws IOException{
        final String preq = "해당 개념을 이용해 주관식 문제와 답 만들어줘. 이 개념과 다른 개념을 이용해서 만들면 좋아. 형식은 문제 : , 답 : 이런식으로 ";
        try{
            Boolean flag = false;
            String question = clovaService.getClovaResponse(file);
            if(question.isEmpty()){
                logger.info("clova ERROR!");
                return new BaseResponse<>(RegularResponseStatus.INTERNAL_SERVER_ERROR.getCode(), "ERROR", RegularResponseStatus.INTERNAL_SERVER_ERROR.getMessage());
            }

            if(flag){
                //다른 문제를 받아오고 싶을 때
                String condition = beforeQuestion + "과 다른 문제를 만들고, 문제의 답 출력해줘";
                beforeQuestion +=  " ," +  condition;
                ChatGptResponseDto responseDto = commonService.getCommonResponse( condition + question, preq);
                QtoQSaveDto saveDto = new QtoQSaveDto();
                String tmpRes = responseDto.getChoices().get(0).getText();
                beforeAnswer = tmpRes;
                saveDto.setOriginalQuestion(changeToString.clovaToString(condition));
                saveDto.setOriginalAnswer(beforeQuestion);
                saveDto.setGeneratedQuestion(responseDto.getChoices().get(0).getText());
                saveDto.setGeneratedAnswer(responseDto.getChoices().get(0).getText());
                sqlService.QtoQSave(saveDto);

                return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto.getChoices().get(0).getText().replaceFirst("^\\n\\n", ""), RegularResponseStatus.OK.getMessage());
            }else{
                //처음 물어본 경우, before Question / answer가 없기 때문에, 도출된 결과 바로 DB 저장
                ChatGptResponseDto responseDto = commonService.getCommonResponse(question, preq);
                String tmpAnswer = responseDto.getChoices().get(0).getText();
                flag = true;
                QtoQSaveDto saveDto = new QtoQSaveDto();
                saveDto.setOriginalQuestion(changeToString.clovaToString(question));
                saveDto.setOriginalAnswer(tmpAnswer); //값 잘 들어가는지 찍어보기
                saveDto.setGeneratedQuestion(changeToString.clovaToString(question));
                saveDto.setGeneratedAnswer(tmpAnswer);
                sqlService.QtoQSave(saveDto);
                return new BaseResponse<>(RegularResponseStatus.OK.getCode(), responseDto.getChoices().get(0).getText().replaceFirst("^\\n\\n", ""), RegularResponseStatus.OK.getMessage());
            }

        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }
}
