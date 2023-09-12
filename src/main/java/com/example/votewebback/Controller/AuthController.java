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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
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
    public ResponseEntity<String> logout(@RequestBody Map<String,String> refresh) throws CustomException {
        String refreshToken = refresh.get("refreshToken");
        Date currentDate = new Date();
        redisService.setDataExpire(refreshToken,"logout",jwtService.getExpirationDateToken(refreshToken).getTime()-currentDate.getTime());
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
    public ResponseEntity<Map<String,Object>> login(@RequestBody RequestDTO.LoginDTO requestLoginDTO){

        return ResponseEntity.ok(authService.authenticate(requestLoginDTO));
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
        String vote_id = code.get("vote_id");
        String student_id = code.get("student_id");

        if(redisService.getData(authCode)==null) {
            throw new CustomException(640,"인증 실패");
        }
        if(vote_id == null && student_id == null){
            return ResponseEntity.ok("투표하기전에 인증 ");
        }else {
            electorService.UpdateEmailConfirm(vote_id, student_id);
            return ResponseEntity.ok("그외 다른 인증");
        }
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
        if(refreshToken != null || !refreshToken.trim().isEmpty()) {
            if (jwtService.isTokenValid(refreshToken)) {
                String dataFromRedis = redisService.getData(refreshToken);
                if(dataFromRedis != null && dataFromRedis.equals("logout")) {
                    throw new CustomException(695, "로그아웃된 토큰");
                }else {
                    String user_id = jwtService.extractUsername(refreshToken);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user_id);
                    if (refreshToken.equals(redisService.getData(user_id))) {
                        String accessToken = jwtService.generateAccessToken(userDetails);
                        return ResponseEntity.ok(new ResponseDTO.LoginDTO(accessToken, ""));
                    } else throw new CustomException(693, "잘못된 인증입니다.");
                }
            } else throw new CustomException(690, "잘못된 JWT 토큰");
        } else throw new CustomException(696, "리프레시 토큰 없음");
    }
}
