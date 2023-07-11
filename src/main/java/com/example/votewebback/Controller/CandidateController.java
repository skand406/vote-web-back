package com.example.votewebback.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    @PostMapping("/register")
    public String CandidateAdd(){
        return "ok";
    }
    @PostMapping("/img/upload")
    public String CandidateSaveImg(){
        return "ok";
    }
    @GetMapping("/img/{vote_id}/{student_id}")
    public String CandidateInfoImg(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @GetMapping("/student")
    public String StudentList(){
        return "ok";
    }
    @GetMapping("/{vote_id}/{student_id}")
    public String CandidateInfo(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @GetMapping("/{vote_id}")
    public String CandidateList(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @PutMapping("/modify/{vote_id}/{student_id}")
    public String CandidateModify(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @PutMapping("/img/modify/{vote_id}/{student_id}")
    public String CandidateModifyImg(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @DeleteMapping("{vote_id}/{student_id}")
    public String CandidateRemove(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }

}
