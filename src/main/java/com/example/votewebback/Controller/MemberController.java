package com.example.votewebback.Controller;

import com.example.votewebback.CustomException;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.CandidateEntity;
import com.example.votewebback.Service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final UserService userService;
    private final VoteService voteService;
    private final ElectorService electorService;
    private final CandidateService candidateService;
    private final StudentService studentService;
    private final EmailService emailService;

    //유저 관련
    @GetMapping("/user/{user_id}")
    public ResponseEntity<ResponseDTO.UserDTO> UserInfo(@PathVariable("user_id") String user_id) throws CustomException {
        ResponseDTO.UserDTO userDTOList = userService.ReadUserById(user_id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userDTOList.getUser_id().equals(authentication.getName())){
            return ResponseEntity.ok(userDTOList);
        } else{
            throw new CustomException(700,"사용할 수 없는 유저 id : " + user_id);
        }
    }
    @PutMapping("/personal/user/{user_id}")
    public ResponseEntity<ResponseDTO.UserDTO> UserModify(@PathVariable("user_id") String user_id,@RequestBody RequestDTO.UserDTO userDTO) throws CustomException {

        userService.UpdateUserPW(user_id,userDTO.getUser_password());
        ResponseDTO.UserDTO responseUserDTO = new ResponseDTO.UserDTO(userService.UpdateUserPersonalInfo(user_id, userDTO));
        return ResponseEntity.ok(responseUserDTO);
    }
    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<String> UserRemove(@PathVariable("user_id") String user_id) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (user_id.equals(authentication.getName())){
            userService.DeleteUser(user_id);
            return ResponseEntity.ok().build();
        } else{
            throw new CustomException(700,"사용할 수 없는 유저 id : " + user_id);
        }
    }

    //투표 관련
    @PostMapping("/personal/vote/register")
    public ResponseEntity<ResponseDTO.VoteDTO> VoteAdd(@RequestBody RequestDTO.VoteDTO requestVoteDTO) throws CustomException {
        ResponseDTO.VoteDTO responseVoteDTO = voteService.CreateVote(requestVoteDTO);
        //request에 vote_id 없어서 response 활용
        electorService.CreateElector(requestVoteDTO.getMajor(), requestVoteDTO.getGrade(), responseVoteDTO.getVote_id());
        return ResponseEntity.ok(responseVoteDTO);
    }
    @GetMapping("/vote/{user_id}") //유저 id로 투표 리스트 찾기
    public ResponseEntity<List<ResponseDTO.VoteDTO>> VoteListByUser(@PathVariable("user_id") String user_id){
        List<ResponseDTO.VoteDTO> responseVoteList = voteService.ReadVoteListByUserId(user_id);
        return ResponseEntity.ok(responseVoteList);
    }
    @PutMapping("/vote/{vote_id}")
    public ResponseEntity<ResponseDTO.VoteDTO> VoteModify(@PathVariable("vote_id") String vote_id, @RequestBody RequestDTO.VoteDTO voteDTO) throws CustomException{
        ResponseDTO.VoteDTO responseVoteDTO = voteService.UpdateVote(vote_id, voteDTO);
        return ResponseEntity.ok(responseVoteDTO);
    }
    @DeleteMapping("/vote/{vote_id}")
    public ResponseEntity<String> VoteRemove(@PathVariable("vote_id") String vote_id) throws CustomException {
        voteService.DeleteVote(vote_id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/vote/result/{vote_id}")
    public ResponseEntity<Map<String, Object>> VoteResult(@PathVariable("vote_id") String vote_id) throws CustomException {
        List<CandidateEntity> electedCandidateList = candidateService.ReadCandidateCount(vote_id);
        if(electedCandidateList.size()==0 ){
            throw new CustomException(660,"선출된 후보 없음");
        }
        Map<String,Object> result = electorService.ReadParticipationRate(vote_id);
        Map<String,Integer> candidate  = new HashMap<>();
        for(CandidateEntity c: electedCandidateList){
            candidate.put(c.getCandidateid(),c.getCandidatecounter());
        }
        result.put("candidate",candidate);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/active/{vote_id}")
    public ResponseEntity<String> VoteStatusModify(@PathVariable("vote_id") String vote_id){
        voteService.UpdateVoteActive(vote_id);
        return ResponseEntity.ok().build();
    }
    //후보 관련
    @PostMapping("/candidate/register")
    public ResponseEntity<Map<String,Object>> CandidateAdd(@RequestBody List<RequestDTO.CandidateDTO> requestCandidateDTOList) throws CustomException {
        for(RequestDTO.CandidateDTO r:requestCandidateDTOList){
            System.out.println(r.getCandidate_id());
        }
        List<ResponseDTO.CandidateDTO> responseCandidateDTOList = new ArrayList<>();
        List<ResponseDTO.StudentDTO> responseStudentDTOList = new ArrayList<>();
        for(RequestDTO.CandidateDTO requestCandidateDTO:requestCandidateDTOList) {
            responseCandidateDTOList.add(candidateService.CreateCandidate(requestCandidateDTO));
            if(candidateService.IsPeopleVote(requestCandidateDTO) != null)
                responseStudentDTOList.add(candidateService.IsPeopleVote(requestCandidateDTO));
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("candidate",responseCandidateDTOList);
        responseMap.put("student",responseStudentDTOList);
        return ResponseEntity.ok(responseMap);
    }
    @PostMapping("/candidate/img/upload")
    public ResponseEntity<String> CandidateSaveImg(@RequestParam String vote_id , @RequestParam List<String> candidate_id, @RequestParam List<MultipartFile> img) throws IOException, CustomException {

        for(int i = 0; i < img.size(); i++){
            MultipartFile f = img.get(i);
            candidateService.CreateImage(f, vote_id, candidate_id.get(i));
        }
        return ResponseEntity.ok().build();
    }
    @PutMapping("/candidate/img/{vote_id}/{candidate_id}")
    public ResponseEntity<String> CandidateModifyImg(@PathVariable("vote_id") String vote_id,@PathVariable("candidate_id") String candidate_id,
                                                     @RequestParam("image") MultipartFile file) throws IOException, CustomException {
        candidateService.UpdateImage(file,vote_id,candidate_id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/candidate/{vote_id}/{candidate_id}")
    public ResponseEntity<String> CandidateRemove(@PathVariable("vote_id") String vote_id,@PathVariable("candidate_id") String candidate_id) throws CustomException {
        candidateService.DeleteCandidate(vote_id,candidate_id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/candidate/{vote_id}/{candidate_id}")
    public ResponseEntity<ResponseDTO.CandidateDTO> CandidateModify(@RequestBody RequestDTO.CandidateDTO requestCandidateDTO,
                                                                    @PathVariable String vote_id, @PathVariable String candidate_id) throws CustomException {
        ResponseDTO.CandidateDTO responseCandidateDTO = candidateService.UpdateCandidate(requestCandidateDTO, vote_id, candidate_id);
        return ResponseEntity.ok(responseCandidateDTO);
    }
    @GetMapping("/candidate/student")
    public ResponseEntity<List<ResponseDTO.StudentDTO>> StudentList(){
        List<ResponseDTO.StudentDTO> responseStudentList = studentService.ReadStudentList();
        return ResponseEntity.ok(responseStudentList);
    }

    //카카오톡 관련
    @GetMapping("/message/URL/{vote_bundle_id}")
    public ResponseEntity<String> UserSendURL(@PathVariable("vote_bundle_id") String vote_bundle_id) throws MessagingException {
        userService.SendVoteUrl(vote_bundle_id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/message/result/{vote_id}")
    public ResponseEntity<String> UserSendResult(@PathVariable("vote_id") String vote_id) throws MessagingException, CustomException {
        userService.SendVoteResult(vote_id);
        return ResponseEntity.ok().build();
    }

}

