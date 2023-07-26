package com.example.votewebback.Service;

import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.UserRepository;
import com.example.votewebback.security.PermissonRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
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
            return "suscces";
        }
        else return "fail: already register manager";
    }



}
