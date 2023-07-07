package com.example.votewebback.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("/{vote_id}/{student_id}")
    public String UserCheck(){
        return "ok";
    }
    @GetMapping("/message/URL/{vote_id}")
    public String UserSendURL(){
        return "ok";
    }
    @GetMapping("/message/result/{vote_id}")
    public String UserSendResult(){
        return "ok";
    }
}
