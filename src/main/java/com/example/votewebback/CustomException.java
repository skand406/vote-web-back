package com.example.votewebback;

import lombok.Getter;

import java.util.Map;
@Getter
public class CustomException extends Exception{
    private final int errorCode;

    public CustomException(int errorCode,String errorMessage) {
        super(errorMessage);
        this.errorCode=errorCode;
    }
}
