package com.example.votewebback.Service;

import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity CreateUser(RequestDTO.UserDTO userDTO, RequestDTO.LoginDTO requestLoginDTO){
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

    public String CheckUserID(String id){
        if(userRepository.findByUserid(id).isEmpty())
            return "ok";
        return "already exits";
    }

}
