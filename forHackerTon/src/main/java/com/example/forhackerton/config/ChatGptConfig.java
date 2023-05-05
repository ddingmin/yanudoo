package com.example.forhackerton.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ChatGptConfig {

    @Value("${chatgpt.api-key}")
    public String API_KEY;

    @Value("${chatgpt.end-point}")
    public String URL;


    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String MODEL = "text-davinci-003";

    public static final Integer MAX_TOKEN = 400; //4096까지 설정 가능 -> 658까지 생성
    public static final Double TEMPERATURE = 0.0; //창의성, 기본 : 0.0
    public static final Double TOP_P = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";



    public String getApiKey() {
        return API_KEY;
    }

    public String getUrl() {
        return URL;
    }
}
