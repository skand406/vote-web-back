package com.example.votewebback.Service;

import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity CreateUser(RequestDTO.UserDTO userDTO, RequestDTO.LoginDTO requestLoginDTO){
        if(!userRepository.findByUserid(requestLoginDTO.user_id()).isEmpty()){
            throw new RuntimeException("이미 가입된 유저입니다.");
        };
           UserEntity user = UserEntity.builder()
                    .useremail(userDTO.getUser_email())
                    .userid(requestLoginDTO.user_id())
                    .username(userDTO.getUser_name())
                    .usertel(userDTO.getUser_tel())
                    .userpassword(passwordEncoder.encode(requestLoginDTO.user_password()))
                    .build();
           userRepository.save(user);
        return user;
    }

}
