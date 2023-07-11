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
    public String VoteResult(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @GetMapping
    public String VoteList(){
        return "ok";
    }
    @GetMapping("/{manager_id}")
    public String VoteList_manager(@PathVariable("manager_id") String manager_id){
        return "ok";
    }
    @GetMapping("/{vote_bundle_id}")
    public String VoteList_bundle(@PathVariable("vote_bundle_id") String vote_bundle_id){
        return "ok";
    }
    @PutMapping("/{vote_id}")
    public String VoteModify(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @DeleteMapping("/{vote_id}")
    public String VoteRemove(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
}
