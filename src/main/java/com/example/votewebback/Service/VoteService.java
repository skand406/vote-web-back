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
        UserEntity user = userRepository.findByUserid(requestVoteDTO.getUser_id()).get();
        if (user == null) {
            Map<Integer,String> error = new HashMap<>();
            error.put(700,"사용할 수 없는 유저 id : " + requestVoteDTO.getUser_id());
            throw new CustomException(error);
            // 또는 원하는 예외 타입을 사용하여 처리할 수 있습니다.
        }else if(requestVoteDTO.getEnd_date().isBefore(requestVoteDTO.getStart_date())){
            Map<Integer,String> error = new HashMap<>();
            error.put(601,"날짜 오류 : " + requestVoteDTO.getStart_date()+"/"+requestVoteDTO.getEnd_date());
            throw new CustomException(error);
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
            Map<Integer,String> error = new HashMap<>();
            error.put(661,"사용할 수 없는 번들 id : " + vote_bundle_id);
            throw new CustomException(error);
        }
        List<ResponseDTO.VoteDTO> responseVoteList = new ArrayList<>();

        for(VoteEntity vote : voteList){
            ResponseDTO.VoteDTO responseVote = new ResponseDTO.VoteDTO(vote);
            responseVoteList.add(responseVote);
        }

        return responseVoteList;
    }

    @Transactional
    public void SumitVote(String candidate_id, String student_id, String vote_id) throws CustomException {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        StudentEntity student = studentRepository.findByStudentid(student_id);
        CandidateEntity candidate=candidateRepository.findByVoteidAndCandidateid(vote,candidate_id).get();
        ElectorEntity elector = electorRepository.findByVoteidAndStudentid(vote,student).get();
        int num = candidate.getCandidatecounter();
        if(!elector.isVoteconfirm()){
            candidate.setCandidatecounter(num+1);
            elector.setVoteconfirm(true);
        }
        else {
            Map<Integer,String> error = new HashMap<>();
            error.put(670,"이미 투표한 유권자" );
            throw new CustomException(error);
        }

    }

    public void DeleteVote(String vote_id) {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).orElseThrow(()->
                new IllformedLocaleException("없는 투표"+vote_id));
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
