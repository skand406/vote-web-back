package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseDTO.UserDTO> signup(@RequestBody RequestDTO.UserDTO requestUserDTO, @RequestBody RequestDTO.LoginDTO requestLoginDTO){
        ResponseDTO.UserDTO responseUserDTO = new ResponseDTO.UserDTO(userService.CreateUser(requestUserDTO,requestLoginDTO));
        return ResponseEntity.ok(responseUserDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDTO.LoginDTO> login(@RequestBody RequestDTO.LoginDTO requestLoginDTO){
        UserEntity user = UserEntity.builder()
                .userid(requestLoginDTO.user_id())
                .userpassword(requestLoginDTO.user_password())
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
