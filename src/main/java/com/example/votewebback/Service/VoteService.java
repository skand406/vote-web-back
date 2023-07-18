package com.example.votewebback.Service;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.UserRepository;
import com.example.votewebback.Repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class VoteService {
    private VoteRepository voteRepository;

    public ResponseDTO.VoteDTO CreateVote(RequestDTO.VoteDTO voteDTO){
        VoteEntity vote = new VoteEntity();
        vote.setEnddate(voteDTO.getEnd_date());
        vote.setStartdate(voteDTO.getStart_date());
        vote.setVoteid("vote_"+RandomCode.randomCode());
        vote.setMajor(voteDTO.getMajor());
        vote.setGrade(vote.getGrade());
        vote.setVotename(voteDTO.getVote_name());
        this.voteRepository.save(vote);

        ResponseDTO.VoteDTO responseVote=new ResponseDTO.VoteDTO(vote);
        return responseVote;
    }
    public ResponseDTO.VoteDTO ReadVoteList(){
        List<VoteEntity> voteEntityList=voteRepository.findAll();
        List<ResponseDTO.VoteDTO> responseVoteList = null;

        for(VoteEntity vote:voteEntityList){
            ResponseDTO.VoteDTO responseVote=new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }


        return responseVoteList;
    }
}
