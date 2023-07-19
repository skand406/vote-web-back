package com.example.votewebback.Service;

import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String createUser(RequestDTO.UserDTO userDTO){
        if(userRepository.findByUserid(userDTO.getUser_id())==null){
            UserEntity user = new UserEntity();
            user.setUseremail(userDTO.getUser_email());
            user.setUserid(userDTO.getUser_email());
            user.setUsername(userDTO.getUser_name());
            user.setUsertel(userDTO.getUser_tel());
            user.setUserpassword(passwordEncoder.encode(userDTO.getUser_password()));
            this.userRepository.save(user);
            return "suscces";
        }
        else return "fail: already register user";
    }
}
