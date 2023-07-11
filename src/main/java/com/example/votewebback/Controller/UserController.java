package com.example.votewebback.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("/{vote_id}/{student_id}")
    public String UserCheck(@PathVariable("vote_id") String vote_id, @PathVariable("student_id") String student_id){
        return "ok";
    }
    @GetMapping("/message/URL/{vote_id}")
    public String UserSendURL(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @GetMapping("/message/result/{vote_id}")
    public String UserSendResult(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
}
