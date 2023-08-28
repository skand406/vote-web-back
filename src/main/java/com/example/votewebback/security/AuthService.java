package com.example.votewebback.security;

import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;

    public ResponseDTO.LoginDTO authenticate(UserEntity user){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserid(), user.getPassword())
                //내부적으로 암호화된 비번 확인 따라서 비번 인코딩 안해도 됨.
        );
        //정상 로직 및 에러 로직 넣어야 함
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        redisService.setDataExpire(user.getUserid(), refreshToken, 1000 * 60 * 60 * 24 * 3);
        return new ResponseDTO.LoginDTO(accessToken, refreshToken);
    }
}
