package com.example.votewebback.Service;

import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String CreateUser(RequestDTO.UserDTO RequestUserDTO) {
        if (userRepository.findByUserid(RequestUserDTO.getUser_id()).isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUseremail(RequestUserDTO.getUser_email());
            user.setUserid(RequestUserDTO.getUser_id());
            user.setUsername(RequestUserDTO.getUser_name());
            user.setUsertel(RequestUserDTO.getUser_tel());
            user.setUserpassword(passwordEncoder.encode(RequestUserDTO.getUser_password()));
            this.userRepository.save(user);
            return "success";
        } else return "fail: already register manager";
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
}