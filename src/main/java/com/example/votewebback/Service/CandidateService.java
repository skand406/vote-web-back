package com.example.votewebback.Service;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public String UploadImage(MultipartFile file,String vote_id,String student_id) throws IOException {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                //확장자 검사
                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                //사진 비율 검사
                BufferedImage image = ImageIO.read(file.getInputStream());
                int width = image.getWidth();
                int height = image.getHeight();
                double targetRatio = 350.0 / 400.0;
                double actualRatio = (double) width / (double) height;

                if ((Math.abs(actualRatio - targetRatio) < 0.01)) {
                    if (!fileExtension.equalsIgnoreCase("png") && !fileExtension.equalsIgnoreCase("jpg")) {
                        return "올바른 이미지 확장자가 아닙니다. (png, jpg 파일만 업로드 가능)";
                    }
                    Path path = Paths.get("D:/img/"+ vote_id + "-" + student_id +".png");
                    Files.write(path, bytes);
                    return "이미지 등록 성공 (크기: "+ width +", "+ height +")";
                } else {
                    return "이미지가 올바르지 않습니다. (원하는 크기: 3.5:4.0)";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "이미지 등록 실패";
            }
        }
        return "이미지 없음";
    }
}
