package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.StudentEntity;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.Repository.StudentRepository;
import com.example.votewebback.Repository.VoteRepository;
import com.example.votewebback.Service.ElectorService;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final ElectorService electorService;


    @GetMapping("/logout")
    public String logout(){
        return "ok";
    }
    @PostMapping("/id_checker")
    public ResponseEntity<String> idChecker(@RequestBody Map<String,String> id){
        String user_id=id.get("user_id");
        return ResponseEntity.ok(userService.CheckUserID(user_id));
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

    @PostMapping("/{vote_id}/{student_id}")
    public ResponseEntity<String> ElectorChecker(@PathVariable("vote_id") String vote_id, @PathVariable("student_id") String student_id,
                                               @RequestBody RequestDTO.UserDTO requestUserDTO){
        String status = electorService.CheckElectorAuthority(vote_id,student_id,requestUserDTO.getUser_email());
        return ResponseEntity.ok(status);
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
