package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.ElectorService;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.Service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;
    private final ElectorService electorService;
    @PostMapping("/register")
    public ResponseDTO.VoteDTO VoteAdd(@RequestBody RequestDTO.VoteDTO requestVoteDTO){
        ResponseDTO.VoteDTO responseVoteDTO = voteService.CreateVote(requestVoteDTO);
        //request에 vote_id 없어서 response 활용
        electorService.CreateElector(requestVoteDTO.getMajor(), requestVoteDTO.getGrade(), responseVoteDTO.getVote_id());
        return responseVoteDTO;
    }
    @GetMapping("/result/{vote_id}")
    public String VoteResult(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @GetMapping
    public List<ResponseDTO.VoteDTO> VoteList(){
        List<ResponseDTO.VoteDTO> voteDTOList=voteService.ReadVoteList();
        return voteDTOList;
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
