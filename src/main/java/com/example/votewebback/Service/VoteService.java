package com.example.votewebback.Service;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.UserRepository;
import com.example.votewebback.Repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;



    public ResponseDTO.VoteDTO CreateVote(RequestDTO.VoteDTO voteDTO){
        VoteEntity vote = new VoteEntity();
        vote.setEnddate(voteDTO.getEnd_date());
        vote.setStartdate(voteDTO.getStart_date());
        vote.setLastenddate(voteDTO.getEnd_date().plusDays(30));
        vote.setVoteid("vote_"+RandomCode.randomCode());
        vote.setVotebundleid(voteDTO.getVote_bundle_id());
        vote.setMajor(voteDTO.getMajor());
        vote.setGrade(voteDTO.getGrade());
        vote.setVotename(voteDTO.getVote_name());
        vote.setVotetype(voteDTO.getVote_type());
        vote.setUserid(userRepository.findByUserid(voteDTO.getUser_id()));
        this.voteRepository.save(vote);

        ResponseDTO.VoteDTO responseVote=new ResponseDTO.VoteDTO(vote);
        return responseVote;
    }
    public List<ResponseDTO.VoteDTO> ReadVoteList(){
        List<VoteEntity> voteList=voteRepository.findAll();
        System.out.println("voteLsit = "+ voteList);
        List<ResponseDTO.VoteDTO> responseVoteList = new ArrayList<>();

        for(VoteEntity vote:voteList){
            ResponseDTO.VoteDTO responseVote=new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }


        return responseVoteList;
    }
}
