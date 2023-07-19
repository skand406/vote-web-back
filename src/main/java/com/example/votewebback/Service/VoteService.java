package com.example.votewebback.Service;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    private VoteRepository voteRepository;

    public ResponseDTO.VoteDTO CreateVote(RequestDTO.VoteDTO voteDTO){
        VoteEntity vote = new VoteEntity();
        vote.setVotename(voteDTO.getVote_name());
        vote.setStartdate(voteDTO.getStart_date());
        vote.setEnddate(voteDTO.getEnd_date());
        vote.setMajor(voteDTO.getMajor());
        vote.setGrade(voteDTO.getGrade());
        vote.setVotebundleid(voteDTO.getVoteBundleid());
        vote.setVoteid("vote_"+RandomCode.randomCode()); //ReatAPI 요청예시에 추가?
        vote.setLastenddate(voteDTO.getEndDate().plusDays(30)); //ReatAPI 요청예시에 추가?

        voteRepository.save(vote);

        ResponseDTO.VoteDTO responseVote = new ResponseDTO.VoteDTO(vote);
        return responseVote;
    }

    public List<ResponseDTO.VoteDTO> ReadVoteList(){
        List<VoteEntity> voteEntityList = voteRepository.findAll();
        List<ResponseDTO.VoteDTO> responseVoteList = null;

        for(VoteEntity vote : voteEntityList){
            ResponseDTO.VoteDTO responseVote = new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }

        return responseVoteList;
    }
}
