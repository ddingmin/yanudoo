package com.example.forhackerton.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionRequestDto implements Serializable {
    private String question;
}
