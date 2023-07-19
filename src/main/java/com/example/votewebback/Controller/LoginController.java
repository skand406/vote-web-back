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

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam("user_id") String user_id,@RequestParam("user_password") String user_password){
        /*String userPassword = requestData.get("user_password");
        String userId = requestData.get("user_id");*/

        Map<String,String> map=new HashMap<String,String>();
        map.put("id",user_id);
        map.put("password",user_password);
        return map;
    }
    @GetMapping("/login/success")
    public ResponseEntity notSession() {
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
        String status= userService.createUser(userDTO);
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
