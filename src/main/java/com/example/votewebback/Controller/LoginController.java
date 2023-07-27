package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.security.AuthService;
import com.example.votewebback.security.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/logout")
    public String logout(){
        return "ok";
    }

    @PostMapping("/signup")
    public String signup(@RequestBody RequestDTO.UserDTO requestUserDTO){
        String status= userService.CreateUser(requestUserDTO);
        System.out.println(status);

        return status;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginDTO.ResponseLogin> login(@RequestBody LoginDTO.RequsetLogin login){
        UserEntity user = UserEntity.builder()
                .userid(login.user_id())
                .userpassword(login.user_password())
                .build();
        return ResponseEntity.ok(authService.authenticate(user));
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
