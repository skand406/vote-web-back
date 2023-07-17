package com.example.votewebback.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elector")
public class ElectorController {
    @PostMapping("/{vote_id}/{student_id}")
    public String UserCheck(@PathVariable("vote_id") String vote_id, @PathVariable("student_id") String student_id){
        return "ok";
    }

}
