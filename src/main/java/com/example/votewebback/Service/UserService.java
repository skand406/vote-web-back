package com.example.votewebback.Service;

import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;

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
    public List<ResponseDTO.UserDTO> SearchUserAll(){
        List<UserEntity> userList = userRepository.findAll();
        List<ResponseDTO.UserDTO> responseUserList = new ArrayList<>();

        for(UserEntity user:userList){
            ResponseDTO.UserDTO responseUser=new ResponseDTO.UserDTO(user);
            responseUserList.add(responseUser);
        }
        return responseUserList;
    }
    public ResponseDTO.UserDTO SearchUserById(String user_id){

        Optional<UserEntity> userOptional = userRepository.findByUserid(user_id);
        ResponseDTO.UserDTO responseUserDTO = new ResponseDTO.UserDTO(userOptional.get());

        return responseUserDTO;
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
    public String CheckUserID(String user_id){
        if(userRepository.findByUserid(user_id).isEmpty())
            return "사용가능한 id입니다.";
        return "중복된 id입니다.";
    }

    @Transactional
    public String UpdateUserPW(String user_id,String user_email){
        UserEntity user=userRepository.findByUserid(user_id).get();
        if(user.getUseremail().equals(user_email)){
            String temPW=RandomCode.randomCode();
            user.setUserpassword(passwordEncoder.encode(temPW));
            emailService.sendMail(user_email,temPW);
            return "임시 비밀번호가 발급되었습니다.";
        }
        else return "이메일이 맞지 않습니다.";
    }

    public String CheckUserEmail(String user_email) {
        if(userRepository.findByUseremail(user_email).isEmpty()) {
            String code = RandomCode.randomCode();
            redisService.setDataExpire(code, user_email, 60 * 5L);
            emailService.sendMail(user_email, code);
            return "인증 번호가 전송되었습니다.";
        }
        else {
            return "이미 가입된 이메일입니다.";
        }
    }
}
