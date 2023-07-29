package com.example.votewebback.Controller;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    @PostMapping("/register")
    public ResponseDTO.CandidateDTO CandidateAdd(@RequestBody RequestDTO.CandidateDTO requestCandidateDTO){
        ResponseDTO.CandidateDTO responseCandidateDTO = candidateService.CreateCandidate(requestCandidateDTO);

        return responseCandidateDTO;
    }
    @PostMapping("/img/upload")
    public ResponseEntity<String> CandidateSaveImg(@RequestParam String vote_id,@RequestParam String student_id,
                                                   @RequestParam("image") MultipartFile file) throws IOException {
        String status = candidateService.CreateImage(file,vote_id,student_id);
        return ResponseEntity.ok(status);
    }
    @GetMapping("/img/{vote_id}/{student_id}")
    public ResponseEntity<byte[]> CandidateInfoImg(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return candidateService.ReadImage(student_id,vote_id);

    }

    @GetMapping("/student")
    public String StudentList(){
        return "ok";
    }
    @GetMapping("/{vote_id}/{student_id}")
    public String CandidateInfo(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @GetMapping("/{vote_id}")
    public String CandidateList(@PathVariable("vote_id") String vote_id){
        return "ok";
    }
    @PutMapping("/modify/{vote_id}/{student_id}")
    public String CandidateModify(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @PutMapping("/img/modify/{vote_id}/{student_id}")
    public String CandidateModifyImg(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }
    @DeleteMapping("{vote_id}/{student_id}")
    public String CandidateRemove(@PathVariable("vote_id") String vote_id,@PathVariable("student_id") String student_id){
        return "ok";
    }

}
