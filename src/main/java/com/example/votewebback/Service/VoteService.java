package com.example.votewebback.Service;

import com.example.votewebback.CustomException;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final ElectorRepository electorRepository;
    private final StudentRepository studentRepository;
    private final CandidateService candidateService;
    private final ElectorService electorService;


    public ResponseDTO.VoteDTO CreateVote(RequestDTO.VoteDTO requestVoteDTO) throws CustomException {
        UserEntity user = userRepository.findByUserid(requestVoteDTO.getUser_id()).orElseThrow(()->
                new CustomException(700,"사용할 수 없는 유저 id : " + requestVoteDTO.getUser_id()));
        if(requestVoteDTO.getEnd_date().isBefore(requestVoteDTO.getStart_date())){
            throw new CustomException(601,"날짜 오류 : " + requestVoteDTO.getStart_date()+"/"+requestVoteDTO.getEnd_date());
        }
        else {
            VoteEntity vote = VoteEntity.builder()
                    .enddate(requestVoteDTO.getEnd_date())
                    .startdate(requestVoteDTO.getStart_date())
                    .lastenddate(requestVoteDTO.getEnd_date().plusDays(30))
                    .voteid("vote_" + RandomCode.randomCode())
                    .votebundleid(requestVoteDTO.getVote_bundle_id())
                    .major(requestVoteDTO.getMajor())
                    .grade(requestVoteDTO.getGrade())
                    .votename(requestVoteDTO.getVote_name())
                    .votetype(requestVoteDTO.getVote_type())
                    .userid(user)
                    .build();
            voteRepository.save(vote);
            ResponseDTO.VoteDTO responseVoteDTO = new ResponseDTO.VoteDTO(vote);
            return responseVoteDTO;
        }
    }
    public List<ResponseDTO.VoteDTO> ReadVoteList(){
        List<VoteEntity> voteList = voteRepository.findAll();
        System.out.println("voteList = "+ voteList);
        List<ResponseDTO.VoteDTO> responseVoteList = new ArrayList<>();

        for(VoteEntity vote:voteList){
            ResponseDTO.VoteDTO responseVote = new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }
        return responseVoteList;
    }
    public List<ResponseDTO.VoteDTO> ReadVoteListByUserId(String user_id){
        Optional<UserEntity> user = userRepository.findByUserid(user_id);
        List<VoteEntity> voteList = voteRepository.findByUserid(user.get());
        List<ResponseDTO.VoteDTO> responseVoteList = new ArrayList<>();

        for(VoteEntity vote : voteList){
            ResponseDTO.VoteDTO responseVote = new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }
        return responseVoteList;
    }
    public List<ResponseDTO.VoteDTO> ReadVoteBundleList(String vote_bundle_id) throws CustomException {
        List<VoteEntity> voteList = voteRepository.findByVotebundleid(vote_bundle_id);
        if(voteList.isEmpty()){
            throw new CustomException(700,"사용할 수 없는 번들 id : " + vote_bundle_id);
        }
        List<ResponseDTO.VoteDTO> responseVoteList = new ArrayList<>();

        for(VoteEntity vote : voteList){
            ResponseDTO.VoteDTO responseVote = new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }

        return responseVoteList;
    }

    @Transactional
    public void SumitVote(List<String> candidate_id_list, String student_id, String vote_id) throws CustomException {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        StudentEntity student = studentRepository.findByStudentid(student_id).get();
        ElectorEntity elector = electorRepository.findByVoteidAndStudentid(vote,student).get();

        List<String> candidateList=candidate_id_list;

        if(!elector.isVoteconfirm()){
            elector.setVoteconfirm(true);
            for(String candidate_id:candidateList){
                CandidateEntity candidate = candidateRepository.findByVoteidAndCandidateid(vote,candidate_id).get();
                int num = candidate.getCandidatecounter();
                candidate.setCandidatecounter(num+1);
            }
        }
        else {
            throw new CustomException(670,"이미 투표한 유권자");
        }
    }

    public void DeleteVote(String vote_id) throws CustomException {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).orElseThrow(()->
                new CustomException(700,"없는 투표"+vote_id));
        voteRepository.delete(vote);
        List<CandidateEntity> candidateList = candidateRepository.findByVoteid(vote);
        for(CandidateEntity c:candidateList){
            candidateService.DeleteCandidate(vote_id,c.getCandidateid());
        }
        List<ElectorEntity> electorList = electorRepository.findByVoteid(vote);
        for(ElectorEntity e:electorList){
            electorService.DeleteElector(vote_id,e.getStudentid());
        }
    }
}
