package com.example.votewebback.Service;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;
    private final StudentRepository studentRepository;



    public ResponseDTO.CandidateDTO CreateCandidate(RequestDTO.CandidateDTO requestCandidateDTO){
        CandidateEntity candidate = new CandidateEntity();
        candidate.setStudentid(studentRepository.findByStudentid(requestCandidateDTO.getStudent_id()));
        candidate.setVoteid(voteRepository.findByVoteid(requestCandidateDTO.getVote_id()));
        candidate.setCandidatespec(requestCandidateDTO.getCandidate_spec());
        candidate.setCandidatepromise(requestCandidateDTO.getCandidate_promise());
        candidate.setCandidatecounter(0); // 득표수 0
        String imgPath = (requestCandidateDTO.getVote_id()) + "-" + (requestCandidateDTO.getStudent_id())+ ".png";
        candidate.setImgpath(imgPath); // img 이름
        this.candidateRepository.save(candidate);

        ResponseDTO.CandidateDTO responseCandidateDTO = new ResponseDTO.CandidateDTO(candidate);
        return responseCandidateDTO;
    }

    @Override
    public String toString() {
        return "";
    }
}
