package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.StudentEntity;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.Repository.StudentRepository;
import com.example.votewebback.Repository.VoteRepository;
import com.example.votewebback.Service.ElectorService;
import com.example.votewebback.Service.RedisService;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final ElectorService electorService;

    private final RedisService redisService;

    @GetMapping("/logout")
    public String logout(){
        return "ok";
    }
    @PostMapping("/id-checker")
    public ResponseEntity<String> IdChecker(@RequestBody Map<String,String> id){
        String user_id=id.get("user_id");
        return ResponseEntity.ok(userService.CheckUserID(user_id));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO.UserDTO> signup(@RequestBody RequestDTO.UserDTO requestUserDTO){
        ResponseDTO.UserDTO responseUserDTO = new ResponseDTO.UserDTO(userService.CreateUser(requestUserDTO));
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

    @PostMapping("/elector/{vote_id}/{student_id}")
    public ResponseEntity<String> ElectorChecker(@PathVariable("vote_id") String vote_id, @PathVariable("student_id") String student_id,
                                                 @RequestBody Map<String,String> email){
        String student_email= email.get("student_email");

        String status = electorService.CheckElectorAuthority(vote_id,student_id,student_email);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/email")
    public ResponseEntity<String> AuthEmail(@RequestBody Map<String,String> code){
        String authCode=code.get("code");
        if(redisService.getData(authCode)==null) return ResponseEntity.ok("인증에 실패했습니다.");

        else return ResponseEntity.ok("인증에 성공했습니다.");
    }
    @PostMapping("/email-checker")
    public ResponseEntity<String> EmailChecker(@RequestBody Map<String,String> email){
        String user_email = email.get("user_email");
        String status = userService.CheckUserEmail(user_email);
        return ResponseEntity.ok(status);
    }


}
