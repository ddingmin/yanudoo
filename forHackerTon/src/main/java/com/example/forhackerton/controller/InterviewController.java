package com.example.forhackerton.controller;


import com.example.forhackerton.common.BaseResponse;
import com.example.forhackerton.config.RegularResponseStatus;
import com.example.forhackerton.data.ChatGptResponseDto;
import com.example.forhackerton.data.InterviewRequestDto;
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
    boolean flag = false;
    int t = 0;
    @ResponseBody
    @PostMapping("/makeChat")
    public BaseResponse chatWithMe(@RequestBody InterviewRequestDto requestDto){
        try{
            ChatMessage systemMessage;
            OpenAiService service = new OpenAiService(token);
            if(!flag && t == 0){
                systemMessage = new ChatMessage(
                        ChatMessageRole.SYSTEM.value(), "너는 사용자와 대화하는 동안 면접관 역할을 한다. 사용자는 이 회사에 입사하고 싶어하는 상황이고, 사용자의 대화를 바탕으로 면접을 진행하면 된다.\n" +
                        "기존에 했던 질문과 유사한 질문들은 하지 않고, 사용자의 대답을 바탕으로 관련된 새로운 질문들을 하는 방식으로 면접을 진행하며, " +
                        "한번에 한가지 질문만 한다." + "너는 " + requestDto.getTmp() + "의 성격으로 대답해주면 돼.");
                flag = true;
                messages.add(systemMessage);
                t++;
            }
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
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), ChatGptResponseDto.builder().build(), RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }

}
