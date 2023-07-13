package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;




    @PostMapping("/id")
    public String ManagerID(){
        return "ok";
    }
    @GetMapping
    public String ManagerList(){
        return "ok";
    }
    @GetMapping("/{manager_id}")
    public String ManagerInfo(@PathVariable("manager_id") String manager_id){
        return "ok";
    }
    @PutMapping("/modify/{manager_id}")
    public String ManagerModify(@PathVariable("manager_id") String manager_id){
        return "ok";
    }
    @PutMapping("/{manager_id}/pw")
    public String ManagerResetPW(@PathVariable("manager_id") String manager_id){
        return "ok";
    }
    @DeleteMapping("/{manager_id}")
    public String ManagerRemove(@PathVariable("manager_id") String manager_id){
        return "ok";
    }

}
