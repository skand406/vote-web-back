package com.example.votewebback.Service;

import com.example.votewebback.CustomException;
import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.UserRepository;
import com.example.votewebback.Repository.VoteRepository;
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
    private final VoteRepository voteRepository;
    private final VoteService voteService;

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
        UserEntity user = userRepository.findByUseremail(RequestUserDTO.getUser_email()).orElseThrow(()->
                new CustomException(632,"가입하지 않은 사용자"));

        String user_id = user.getUserid();
        if (user.getUserName().equals(RequestUserDTO.getUser_name())
                && user.getUsertel().equals(RequestUserDTO.getUser_tel()))
            return user_id;
        else {
            throw new CustomException(631, "이름과 번호가 일치하지 않음"+RequestUserDTO.getUser_name() +"/"+RequestUserDTO.getUser_tel());
        }
    }
    public void CheckUserID(String user_id) throws CustomException {
        if(!userRepository.findByUserid(user_id).isEmpty()) {
            throw new CustomException(621, "중복된 id");
        }
    }

    @Transactional
    public void UpdateUserPW(String user_id,String user_email) throws CustomException {
        UserEntity user=userRepository.findByUserid(user_id).orElseThrow(()->
                new CustomException(700,"없는 유저"+user_id));
        if(!user.getUseremail().equals(user_email)){
            throw new CustomException(680,"이메일이 맞지 않음");
        }
        String temPW=RandomCode.randomCode();
        user.setUserpassword(passwordEncoder.encode(temPW));
        emailService.sendMail(user_email,temPW,"pw");
    }

    public void CheckUserEmail(String user_email) throws CustomException {
        if(!userRepository.findByUseremail(user_email).isEmpty()) {
            throw new CustomException(650,"이미 가입된 이메일 " +user_email);
        }
        String code = RandomCode.randomCode();
        redisService.setDataExpire(code, user_email, 60 * 5L);
        emailService.sendMail(user_email, code,"email");
    }

    public void DeleteUser(String user_id) throws CustomException {
        UserEntity user = userRepository.findByUserid(user_id).orElseThrow(()->
                new CustomException(700,"없는 회원"+user_id));
        userRepository.delete(user);
        List<VoteEntity> voteList = voteRepository.findByUserid(user);
        for(VoteEntity v:voteList){
            voteService.DeleteVote(v.getVoteid());
        }
    }
}
