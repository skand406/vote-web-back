package com.example.votewebback.Controller;

import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.Service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {
    private final UserService userService;
    private final VoteService voteService;

    @GetMapping("/user")
    public List<ResponseDTO.UserDTO> UserList(){
        List<ResponseDTO.UserDTO> userDTOList = userService.ReadUserAll();
        return userDTOList;
    }

    @GetMapping("/vote")
    public List<ResponseDTO.VoteDTO> VoteList(){
        List<ResponseDTO.VoteDTO> voteDTOList=voteService.ReadVoteList();
        return voteDTOList;
    }

}
