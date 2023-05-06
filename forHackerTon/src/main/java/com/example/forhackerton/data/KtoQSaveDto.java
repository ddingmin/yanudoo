package com.example.forhackerton.data;

import lombok.Data;

@Data
public class KtoQSaveDto {
    private String keyword;
    private Integer searchCount;
    private String generatedQuestion;
    private String generatedAnswer;
}
