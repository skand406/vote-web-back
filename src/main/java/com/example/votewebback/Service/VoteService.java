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



    public ResponseDTO.VoteDTO CreateVote(RequestDTO.VoteDTO requestVoteDTO){
        VoteEntity vote = new VoteEntity();
        vote.setEnddate(requestVoteDTO.getEnd_date());
        vote.setStartdate(requestVoteDTO.getStart_date());
        vote.setLastenddate(requestVoteDTO.getEnd_date().plusDays(30));
        vote.setVoteid("vote_"+RandomCode.randomCode());
        vote.setVotebundleid(requestVoteDTO.getVote_bundle_id());
        vote.setMajor(requestVoteDTO.getMajor());
        vote.setGrade(requestVoteDTO.getGrade());
        vote.setVotename(requestVoteDTO.getVote_name());
        vote.setVotetype(requestVoteDTO.getVote_type());
        vote.setUserid(userRepository.findByUserid(requestVoteDTO.getUser_id()).get());
        this.voteRepository.save(vote);

        ResponseDTO.VoteDTO responseVoteDTO = new ResponseDTO.VoteDTO(vote);
        return responseVoteDTO;
    }
    public List<ResponseDTO.VoteDTO> ReadVoteList(){
        List<VoteEntity> voteList=voteRepository.findAll();
        System.out.println("voteList = "+ voteList);
        List<ResponseDTO.VoteDTO> responseVoteList = new ArrayList<>();

        for(VoteEntity vote:voteList){
            ResponseDTO.VoteDTO responseVote=new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }
        return responseVoteList;
    }

    public List<ResponseDTO.VoteDTO> ReadVoteBundleList(String vote_bundle_id){
        List<VoteEntity> voteList = voteRepository.findByVotebundleid(vote_bundle_id);
        List<ResponseDTO.VoteDTO> responseVoteList = new ArrayList<>();

        for(VoteEntity vote : voteList){
            ResponseDTO.VoteDTO responseVote = new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }

        return responseVoteList;
    }
}
