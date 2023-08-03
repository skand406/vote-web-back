package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final VoteService voteService;
    private final ElectorService electorService;
    private final CandidateService candidateService;
    private final StudentService studentService;

    //유저 관련
    @GetMapping("/user/{user_id}")
    public ResponseDTO.UserDTO UserInfo(@PathVariable("user_id") String user_id){
        ResponseDTO.UserDTO userDTOList = userService.SearchUserById(user_id);
        return userDTOList;
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
    public ResponseDTO.VoteDTO VoteAdd(@RequestBody RequestDTO.VoteDTO requestVoteDTO){
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
    public ResponseDTO.CandidateDTO CandidateAdd(@RequestBody RequestDTO.CandidateDTO requestCandidateDTO){
        ResponseDTO.CandidateDTO responseCandidateDTO = candidateService.CreateCandidate(requestCandidateDTO);

        return responseCandidateDTO;
    }
    @PostMapping("/candidate/img/upload")
    public ResponseEntity<String> CandidateSaveImg(@RequestParam String vote_id, @RequestParam String student_id,
                                                   @RequestParam("image") MultipartFile file) throws IOException {
        String status = candidateService.CreateImage(file,vote_id,student_id);
        return ResponseEntity.ok(status);
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

