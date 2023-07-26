package com.example.votewebback.security;

import lombok.Getter;

@Getter
public class LoginDTO {
    public record RequsetLogin(String user_id,String user_password){}
    public record ResponseLogin(String token){}
}
