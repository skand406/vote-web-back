package com.example.votewebback.Controller;

import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<ResponseDTO.UserDTO> UserList(){
        List<ResponseDTO.UserDTO> userDTOList = userService.SearchUserAll();
        return userDTOList;
    }
    @GetMapping("/{user_id}")
    public ResponseDTO.UserDTO UserInfo(@PathVariable("user_id") String user_id){
        ResponseDTO.UserDTO userDTOList = userService.SearchUserById(user_id);
        return userDTOList;
    }
    @PutMapping("/modify/{user_id}")
    public String UserModify(@PathVariable("user_id") String user_id){
        return "ok";
    }

    @DeleteMapping("/{user_id}")
    public String UserRemove(@PathVariable("user_id") String user_id){
        return "ok";
    }

}
