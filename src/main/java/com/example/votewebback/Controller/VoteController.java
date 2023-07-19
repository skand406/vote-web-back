package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.VoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private VoteService voteService;

    @PostMapping("/register")
    public ResponseDTO.VoteDTO VoteAdd(@RequestBody RequestDTO.VoteDTO requestVoteDTO){
        ResponseDTO.VoteDTO responseVoteDTO = voteService.CreateVote(requestVoteDTO);
        return responseVoteDTO;
    }
    @GetMapping("/result/{vote_id}")
    public String VoteResult(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @GetMapping
    public List<ResponseDTO.VoteDTO> VoteList(){
        List<ResponseDTO.VoteDTO> responseVoteDTOList = voteService.ReadVoteList();
        return responseVoteDTOList;
    }
    @GetMapping("/{user_id}")
    public String VoteList_user(@PathVariable("user_id") String user_id){
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
