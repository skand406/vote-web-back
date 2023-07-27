package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;




    @PostMapping("/id")
    public ResponseEntity<String> UserID(@RequestBody RequestDTO.UserDTO requestUserDTO){
        return ResponseEntity.ok(userService.SearchUserid(requestUserDTO));
    }
    @GetMapping
    public String UserList(){
        return "ok";
    }
    @GetMapping("/{user_id}")
    public String UserInfo(@PathVariable("user_id") String user_id){
        return "ok";
    }
    @PutMapping("/modify/{user_id}")
    public String UserModify(@PathVariable("user_id") String user_id){
        return "ok";
    }
    @PutMapping("/{user_id}/pw")
    public String UserResetPW(@PathVariable("user_id") String user_id){
        return "ok";
    }
    @DeleteMapping("/{user_id}")
    public String UserRemove(@PathVariable("user_id") String user_id){
        return "ok";
    }

}
