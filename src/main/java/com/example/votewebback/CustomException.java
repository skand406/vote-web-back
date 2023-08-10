package com.example.votewebback;

import lombok.Getter;

import java.util.Map;
@Getter
public class CustomException extends Exception{
    private final Map<Integer,String> error;
    public CustomException(Map<Integer,String> error) {
        this.error =error;
    }
}
