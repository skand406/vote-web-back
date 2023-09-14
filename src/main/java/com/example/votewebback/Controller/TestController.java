package com.example.votewebback.Controller;

import com.example.votewebback.DTO.ResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
@Hidden // 스웨거 문서 숨김
@RestController
@RequestMapping("/test")
public class TestController {
   /* @GetMapping("/get")
    public ResponseDTO.VoteDTO getTest(){
        ResponseDTO.VoteDTO vote;

        return vote;
    }*/
    @GetMapping("/{id}")
    public int getTest_path(@PathVariable("id") int id){

        return id;
    }
    @GetMapping("/all")
    public String test1(){
        return "모든 접근";
    }
    @GetMapping("/auth")
    public String test2(){
        return "제한 접근";
    }
    @PostMapping("/post")
    public Map<String,String> postTest(@RequestBody Map<String, String> requestData){
        String userPassword = requestData.get("user_password");
        String userId = requestData.get("user_id");

        Map<String,String> map=new HashMap<String,String>();
        map.put("user_id", userId);
        map.put("user_password", userPassword);
        return map;
    }
}
