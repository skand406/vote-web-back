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
    public String CandidateInfoImg(){
        return "ok";
    }
    @GetMapping("/student")
    public String StudentList(){
        return "ok";
    }
    @GetMapping("/{vote_id}/{student_id}")
    public String CandidateInfo(){
        return "ok";
    }
    @GetMapping("/{vote_id}")
    public String CandidateList(){
        return "ok";
    }
    @PutMapping("/modify/{vote_id}/{student_id}")
    public String CandidateModify(){
        return "ok";
    }
    @PutMapping("/img/modify/{vote_id}/{student_id}")
    public String CandidateModifyImg(){
        return "ok";
    }
    @DeleteMapping("{vote_id}/{student_id}")
    public String CandidateRemove(){
        return "ok";
    }

}
