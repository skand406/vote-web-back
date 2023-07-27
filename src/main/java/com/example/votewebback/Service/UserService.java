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

import java.util.Optional;


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
    public String SearchUserid(RequestDTO.UserDTO RequestUserDTO) {
        Optional<UserEntity> user = userRepository.findByUseremail(RequestUserDTO.getUser_email());
        if (user.isEmpty()) {
            return "가입한적 없는 이메일입니다.";
        } else {
            String user_id = user.get().getUserid();
            if (user.get().getUserName().equals(RequestUserDTO.getUser_name())
                    && user.get().getUsertel().equals(RequestUserDTO.getUser_tel()))
                return user_id;
            else
                return "입력한 이메일이 저장된 이름과 전화번호가 일치하지 않습니다.";
        }
    }
    public String CheckUserID(String id){
        if(userRepository.findByUserid(id).isEmpty())
            return "ok";
        return "already exits";
    }

}
