package com.example.votewebback.Controller;

import com.example.votewebback.DTO.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/get")
    public ResponseDTO.VoteDTO getTest(){
        ResponseDTO.VoteDTO vote = new ResponseDTO.VoteDTO();

        return vote;
    }
    @GetMapping("/{id}")
    public int getTest_path(@PathVariable("id") int id){

        return id;
    }
    @GetMapping("/user")
    public String test(Principal user){
        return "user만 접근";
    }
    @PostMapping("/post")
    public Map<String,String> postTest(@RequestBody Map<String, String> requestData){
        String managerPassword = requestData.get("manager_password");
        String managerId = requestData.get("manager_id");

        Map<String,String> map=new HashMap<String,String>();
        map.put("manager_id",managerId);
        map.put("manager_password",managerPassword);
        return map;
    }
}
