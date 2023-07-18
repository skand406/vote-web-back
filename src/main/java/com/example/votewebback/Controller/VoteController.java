package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseDTO.VoteDTO VoteAdd(@RequestBody RequestDTO.VoteDTO requestVoteDTO){
        ResponseDTO.VoteDTO responseVoteDTO=userService.CreateVote(requestVoteDT0);
        return responseVoteDTO;
    }
    @GetMapping("/result/{vote_id}")
    public String VoteResult(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @GetMapping
    public List<ResponseDTO.VoteDTO> VoteList(){
        List<ResponseDTO.VoteDTO> voteDTOList=userService.ReadVoteList();
        return voteDTOList;
    }
    @GetMapping("/{manager_id}")
    public String VoteList_manager(@PathVariable("manager_id") String manager_id){
        return "ok";
    }
    @GetMapping("/{vote_bundle_id}")
    public String VoteList_bundle(@PathVariable("vote_bundle_id") String vote_bundle_id){
        return "ok";
    }
    @PutMapping("/{vote_id}")
    public String VoteModify(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @DeleteMapping("/{vote_id}")
    public String VoteRemove(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
}
