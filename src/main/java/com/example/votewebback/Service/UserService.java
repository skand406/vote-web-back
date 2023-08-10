package com.example.votewebback.Service;

import com.example.votewebback.CustomException;
import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;

    public UserEntity CreateUser(RequestDTO.UserDTO userDTO){
        UserEntity user = UserEntity.builder()
                .useremail(userDTO.getUser_email())
                .userid(userDTO.getUser_id())
                .username(userDTO.getUser_name())
                .usertel(userDTO.getUser_tel())
                .userpassword(passwordEncoder.encode(userDTO.getUser_password()))
                .build();
        userRepository.save(user);
        return user;
    }
    public List<ResponseDTO.UserDTO> ReadUserAll(){
        List<UserEntity> userList = userRepository.findAll();
        List<ResponseDTO.UserDTO> responseUserList = new ArrayList<>();

        for(UserEntity user:userList){
            ResponseDTO.UserDTO responseUser=new ResponseDTO.UserDTO(user);
            responseUserList.add(responseUser);
        }
        return responseUserList;
    }
    public ResponseDTO.UserDTO ReadUserById(String user_id){
            Optional<UserEntity> userOptional = userRepository.findByUserid(user_id);
            ResponseDTO.UserDTO responseUserDTO = new ResponseDTO.UserDTO(userOptional.get());
            return responseUserDTO;
        }

    public String SearchUserid(RequestDTO.UserDTO RequestUserDTO) throws CustomException {
        Optional<UserEntity> user = userRepository.findByUseremail(RequestUserDTO.getUser_email());
        if (user.isEmpty()) {
            Map<Integer,String> error = new HashMap<>();
            error.put(632,"가입하지 않은 사용자");
            throw new CustomException(error);
        } else {
            String user_id = user.get().getUserid();
            if (user.get().getUserName().equals(RequestUserDTO.getUser_name())
                    && user.get().getUsertel().equals(RequestUserDTO.getUser_tel()))
                return user_id;
            else {
                Map<Integer, String> error = new HashMap<>();
                error.put(631, "이름과 번호가 일치하지 않음"+RequestUserDTO.getUser_name() +"/"+RequestUserDTO.getUser_tel());
                throw new CustomException(error);
            }
        }
    }
    public void CheckUserID(String user_id) throws CustomException {
        if(!userRepository.findByUserid(user_id).isEmpty()) {
            Map<Integer, String> error = new HashMap<>();
            error.put(621, "중복된 id");
            throw new CustomException(error);
        }
    }

    @Transactional
    public String UpdateUserPW(String user_id,String user_email){
        UserEntity user=userRepository.findByUserid(user_id).get();
        if(user.getUseremail().equals(user_email)){
            String temPW=RandomCode.randomCode();
            user.setUserpassword(passwordEncoder.encode(temPW));
            emailService.sendMail(user_email,temPW,"pw");
            return "임시 비밀번호가 발급되었습니다.";
        }
        else return "이메일이 맞지 않습니다.";
    }

    public void CheckUserEmail(String user_email) throws CustomException {
        if(!userRepository.findByUseremail(user_email).isEmpty()) {
            Map<Integer,String> error = new HashMap<>();
            error.put(650,"이미 가입된 이메일 " +user_email);
            throw new CustomException(error);
        }
        String code = RandomCode.randomCode();
        redisService.setDataExpire(code, user_email, 60 * 5L);
        emailService.sendMail(user_email, code,"email");
    }
}
