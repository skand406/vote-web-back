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

    public String CreateUser(RequestDTO.UserDTO userDTO){
        if(userRepository.findByUserid(userDTO.getUser_id()).isEmpty()){
            UserEntity user = new UserEntity();
            user.setUseremail(userDTO.getUser_email());
            user.setUserid(userDTO.getUser_id());
            user.setUsername(userDTO.getUser_name());
            user.setUsertel(userDTO.getUser_tel());
            user.setUserpassword(passwordEncoder.encode(userDTO.getUser_password()));
            this.userRepository.save(user);
            return "success";
        }
        else return "fail: already register manager";
    }


}
