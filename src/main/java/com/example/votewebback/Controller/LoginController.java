package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    /*@PostMapping("/login")
    public String login(@RequestBody Map<String,String> requestData){
        String managerPassword = requestData.get("manager_password");
        String managerId = requestData.get("manager_id");

        return "ok";
    }*/
    @GetMapping("/login/success")
    public ResponseEntity notSesstion() {
        System.out.println("로그인 성공");
        Map<String,Object> map = new HashMap<>();
        map.put("result", 1);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @GetMapping("/login/fail")
    public ResponseEntity hello() {
        System.out.println("로그인 실패");
        Map<String,Object> map = new HashMap<>();
        map.put("result", 0);
        return new ResponseEntity(map, HttpStatus.OK);
    }
    @GetMapping("/user")
    public String test(Principal user){
        return "user만 접근";
    }
    @GetMapping("/logout")
    public String logout(){
        return "ok";
    }
    @PostMapping("/signup")
    public String signup(@RequestBody RequestDTO.UserDTO userDTO)
    {
        String status= userService.createManager(userDTO);
        System.out.println(status);

        return status;
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
