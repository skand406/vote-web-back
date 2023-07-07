package com.example.votewebback.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers")
public class ManagerController {
    @PostMapping("/login")
    public String login(){
        return "ok";
    }
    @PostMapping("/signup")
    public String signup(){
        return "ok";
    }
    @PostMapping("/id")
    public String ManagerID(){
        return "ok";
    }
    @GetMapping
    public String ManagerList(){
        return "ok";
    }
    @GetMapping("/{manager_id}")
    public String ManagerInfo(){
        return "ok";
    }
    @PutMapping("/modify/{manager_id}")
    public String ManagerModify(){
        return "ok";
    }
    @PutMapping("/{manager_id}/pw")
    public String ManagerResetPW(){
        return "ok";
    }
    @DeleteMapping("{manager_id}")
    public String ManagerRemove(){
        return "ok";
    }

}
