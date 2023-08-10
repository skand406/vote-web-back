package com.example.votewebback.Controller;

import com.example.votewebback.CustomException;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.*;
import com.example.votewebback.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


import java.io.IOException;
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

    //유저 관련
    @GetMapping("/user/{user_id}")
    public ResponseEntity<ResponseDTO.UserDTO> UserInfo(@PathVariable("user_id") String user_id) throws CustomException {
        ResponseDTO.UserDTO userDTOList = userService.ReadUserById(user_id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userDTOList.getUser_id().equals(authentication.getName())){
            return ResponseEntity.ok(userDTOList);
        } else{
            System.out.println(userDTOList.getUser_id());
            System.out.println(authentication.getName());
            Map<Integer,String> error = new HashMap<>();
            error.put(700,"사용할 수 없는 유저 id : " + user_id);
            throw new CustomException(error);
        }
    }
    @PutMapping("/user/{user_id}")
    public String UserModify(@PathVariable("user_id") String user_id){
        return "ok";
    }
    @DeleteMapping("/user/{user_id}")
    public String UserRemove(@PathVariable("user_id") String user_id){
        return "ok";
    }

    //투표 관련
    @PostMapping("/vote/register")
    public ResponseDTO.VoteDTO VoteAdd(@RequestBody RequestDTO.VoteDTO requestVoteDTO) throws CustomException {
        ResponseDTO.VoteDTO responseVoteDTO = voteService.CreateVote(requestVoteDTO);
        //request에 vote_id 없어서 response 활용
        electorService.CreateElector(requestVoteDTO.getMajor(), requestVoteDTO.getGrade(), responseVoteDTO.getVote_id());
        return responseVoteDTO;
    }
    @GetMapping("/vote/{user_id}") //유저 id로 투표 리스트 찾기
    public List<ResponseDTO.VoteDTO> VoteListByUser(@PathVariable("user_id") String user_id){
        List<ResponseDTO.VoteDTO> responseVoteList = voteService.ReadVoteListByUserId(user_id);
        return responseVoteList;
    }
    @PutMapping("/vote/{vote_id}")
    public String VoteModify(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @DeleteMapping("/vote/{vote_id}")
    public String VoteRemove(@PathVariable("vote_id") String vote_id){
        return "ok";
    }

    //후보 관련
    @PostMapping("/candidate/register")
    public ResponseEntity<Map<String,Object>> CandidateAdd(@RequestBody RequestDTO.CandidateDTO requestCandidateDTO) throws CustomException {
        ResponseDTO.CandidateDTO responseCandidateDTO = candidateService.CreateCandidate(requestCandidateDTO);
        ResponseDTO.StudentDTO responseStudentDTO =candidateService.IsPeopleVote(requestCandidateDTO);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("candidate",responseCandidateDTO);
        responseMap.put("student",responseStudentDTO);
        return ResponseEntity.ok(responseMap);
    }
    @PostMapping("/candidate/img/upload")
    public ResponseEntity<String> CandidateSaveImg(@RequestParam String vote_id, @RequestParam String student_id,
                                                   @RequestParam("image") MultipartFile file) throws IOException, CustomException {
        candidateService.CreateImage(file,vote_id,student_id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/candidate/img/{vote_id}/{student_id}")
    public ResponseEntity<String> CandidateModifyImg(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id,
                                                     @RequestParam("image") MultipartFile file)throws IOException{
        String status = candidateService.UpdateImage(file,vote_id,student_id);

        return ResponseEntity.ok(status);
    }
    @DeleteMapping("/candidate/{vote_id}/{student_id}")
    public String CandidateRemove(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @PutMapping("/candidate/{vote_id}/{student_id}")
    public String CandidateModify(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @GetMapping("/candidate/student")
    public List<ResponseDTO.StudentDTO> StudentList(){
        List<ResponseDTO.StudentDTO> responseStudentList = studentService.ReadStudentList();
        return responseStudentList;
    }

    //카카오톡 관련
    @GetMapping("/message/URL/{vote_id}")
    public String UserSendURL(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @GetMapping("/message/result/{vote_id}")
    public String UserSendResult(@PathVariable("vote_id") String vote_id){
        return "ok";
    }

}

