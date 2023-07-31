package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PublicContorller {
    private final UserService userService;

    @PostMapping("user/id")
    public ResponseEntity<String> UserID(@RequestBody RequestDTO.UserDTO requestUserDTO){
        return ResponseEntity.ok(userService.SearchUserid(requestUserDTO));
    }
    @PutMapping("user/pw")
    public ResponseEntity<String> UserResetPW(@RequestBody Map<String,String> user){
        String user_id = user.get("user_id");
        String user_email = user.get("user_email");
        String status = userService.UpdateUserPW(user_id,user_email);
        return ResponseEntity.ok(status);
    }
}
