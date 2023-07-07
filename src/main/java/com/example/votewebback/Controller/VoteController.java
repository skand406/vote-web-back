package com.example.votewebback.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
public class VoteController {
    @PostMapping("/register")
    public String VoteAdd(){
        return "ok";
    }
    @GetMapping("/result/{vote_id}")
    public String VoteResult(){
        return "ok";
    }
    @GetMapping
    public String VoteList(){
        return "ok";
    }
    @GetMapping("/{manager_id")
    public String VoteList_manager(){
        return "ok";
    }
    @GetMapping("/{vote_bundle_id}")
    public String VoteList_bundle(){
        return "ok";
    }
    @PutMapping("/{vote_id}")
    public String VoteModify(){
        return "ok";
    }
    @DeleteMapping("/{vote_id}")
    public String VoteRemove(){
        return "ok";
    }
}
