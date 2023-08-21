package com.example.votewebback.Controller;

import com.example.votewebback.CustomException;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Service.ElectorService;
import com.example.votewebback.Service.RedisService;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.security.AuthService;
import com.example.votewebback.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final ElectorService electorService;
    private final JwtService jwtService;
    private final RedisService redisService;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String,String> refresh){
        String refreshToken = refresh.get("refreshToken");
        redisService.setData(refreshToken,"logout");
        return ResponseEntity.ok().build();
    }
    @PostMapping("/id-checker")
    public ResponseEntity<String> IdChecker(@RequestBody Map<String,String> id) throws CustomException {
        String user_id=id.get("user_id");
        userService.CheckUserID(user_id);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<String> AuthEmail(@RequestBody Map<String,String> code) throws CustomException {
        String authCode=code.get("code");
        if(redisService.getData(authCode)==null) {
            throw new CustomException(640,"인증 실패");
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("/email-checker")
    public ResponseEntity<String> EmailChecker(@RequestBody Map<String,String> email) throws CustomException {
        String user_email = email.get("user_email");
        userService.CheckUserEmail(user_email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO.LoginDTO> RefreshTokenAdd(@RequestBody Map<String,String> refresh) throws CustomException {
        String refreshToken = refresh.get("refreshToken");
        if(!redisService.getData(refreshToken).equals("logout")) {
            String user_id = jwtService.extractUsername(refreshToken);
            System.out.println(user_id);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user_id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && jwtService.isTokenValid(refreshToken, userDetails)) {
                System.out.println(authentication.getName());
                if (refreshToken.equals(redisService.getData(authentication.getName()))) {
                    String accesstoken = jwtService.generateAccessToken(userDetails);
                    return ResponseEntity.ok(new ResponseDTO.LoginDTO(accesstoken, ""));
                } else throw new CustomException(690, "잘못된 인증입니다.");
            } else throw new CustomException(691, "잘못된 토큰입니다.");
        }
        else throw new CustomException(691, "잘못된 토큰입니다.");
    }
}
