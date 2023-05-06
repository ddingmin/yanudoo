package com.example.forhackerton.data;

import lombok.Data;

@Data
public class QtoQSaveDto {
    private String originalQuestion;
    private String originalAnswer;
    private String generatedQuestion;
    private String generatedAnswer;
}
