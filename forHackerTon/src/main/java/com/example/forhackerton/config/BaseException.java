package com.example.forhackerton.config;

import lombok.Data;

@Data
public class BaseException extends RuntimeException{

    private final RegularResponseStatus status;

    public BaseException(RegularResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
