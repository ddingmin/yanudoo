package com.example.forhackerton.common;

import com.example.forhackerton.config.RegularResponseStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Data
public class BaseResponse<T> {

    private int status;

    private T data;

    private String message;

    public BaseResponse(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(T data) {
        this.status = HttpStatus.OK.value();
        this.data = data;
        this.message = "success";
    }

    public BaseResponse(RegularResponseStatus status) {
        this.status = status.getCode();
        this.message = status.getMessage();
    }
}
