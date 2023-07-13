package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.Service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final ManagerService managerService;

    @PostMapping("/login")
    public String login(@RequestBody Map<String,String> requestData){
        String managerPassword = requestData.get("manager_password");
        String managerId = requestData.get("manager_id");

        return "ok";
    }
    @GetMapping("/logout")
    public String logout(){
        return "ok";
    }
    @PostMapping("/signup")
    public String signup(@RequestBody RequestDTO.ManagerDTO managerDTO)
    {
        String status=managerService.createManager(managerDTO);
        System.out.println(status);

        return status;
    }
}
