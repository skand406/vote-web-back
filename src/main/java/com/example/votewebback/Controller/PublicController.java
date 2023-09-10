package com.example.votewebback.Controller;

import com.example.votewebback.CustomException;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.CandidateService;
import com.example.votewebback.Service.ElectorService;
import com.example.votewebback.Service.UserService;
import com.example.votewebback.Service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/publices")
public class PublicController {
    private final UserService userService;
    private final VoteService voteService;
    private final CandidateService candidateService;
    private final ElectorService electorService;

    //유저 관련
    @PostMapping("/user/id")
    public ResponseEntity<String> UserID(@RequestBody RequestDTO.UserDTO requestUserDTO) throws CustomException {
        return ResponseEntity.ok(userService.SearchUserid(requestUserDTO));
    }
    @PutMapping("/user/pw")
    public ResponseEntity<String> UserResetPW(@RequestBody Map<String,String> user) throws CustomException {
        String user_id = user.get("user_id");
        String user_email = user.get("user_email");
        userService.UpdateResetUserPW(user_id,user_email);
        return ResponseEntity.ok().build();
    }

    //투표 관련
    @GetMapping("/vote/{vote_bundle_id}") //투표 번들 id로 투표 리스트 찾기
    public List<ResponseDTO.VoteDTO> VoteListByBundle(@PathVariable("vote_bundle_id") String vote_bundle_id) throws CustomException {
        List<ResponseDTO.VoteDTO> voteDTOList = voteService.ReadVoteBundleList(vote_bundle_id);
        return voteDTOList;
    }


    @PutMapping("/vote/{vote_id}")
    public ResponseEntity<String> Vote(@PathVariable("vote_id") String vote_id,@RequestBody Map<String,List<String>> id) throws CustomException {
        List<String> candidate_id_list = id.get("candidate_id");
        String student_id = id.get("student_id").get(0);
        System.out.println(student_id);
        voteService.SumitVote(candidate_id_list,student_id,vote_id);
        return ResponseEntity.ok().build();
    }

    //후보 관련
    @GetMapping("candidate/img/{vote_id}/{student_id}")
    public ResponseEntity<byte[]> CandidateInfoImg(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id) throws CustomException {
        return candidateService.ReadImage(student_id,vote_id);

    }
    @GetMapping("candidate/{vote_id}/{student_id}")
    public ResponseEntity<Map<String,Object>> CandidateInfo(@PathVariable("vote_id") String vote_id,@PathVariable("candidate_id") String candidate_id){
        return ResponseEntity.ok(candidateService.ReadCandidate(vote_id, candidate_id));
    }
    @GetMapping("candidate/{vote_id}")
    public List<ResponseDTO.CandidateDTO> CandidateList(@PathVariable("vote_id") String vote_id){
        List<ResponseDTO.CandidateDTO> responseCandidateList = candidateService.ReadCandidateListByVoteId(vote_id);
        return responseCandidateList;
    }

}
