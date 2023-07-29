package com.example.votewebback.Service;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import org.apache.commons.io.IOUtils;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;
    private final StudentRepository studentRepository;
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


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

    public String CreateImage(MultipartFile file, String vote_id, String student_id) throws IOException {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                //확장자 검사
                String fileExtension = file.getContentType();
                //사진 비율 검사
                BufferedImage image = ImageIO.read(file.getInputStream());
                int width = image.getWidth();
                int height = image.getHeight();
                double targetRatio = 350.0 / 400.0;
                double actualRatio = (double) width / (double) height;

                if ((Math.abs(actualRatio - targetRatio) < 0.01)) {
                    if (!fileExtension.equalsIgnoreCase("image/png") && !fileExtension.equalsIgnoreCase("image/jpeg")) {
                        return "올바른 이미지 확장자가 아닙니다. (png, jpg 파일만 업로드 가능)";
                    }

                    String fileName= "img/" + vote_id + "-" + student_id;
                    String fileUrl= "https://" + bucket + "/" + fileName;
                    ObjectMetadata metadata= new ObjectMetadata();
                    metadata.setContentType(file.getContentType());
                    metadata.setContentLength(file.getSize());
                    amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);

                    return "이미지 등록 성공 (크기: "+ width +", "+ height +")\n"
                            + fileUrl;
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
    public ResponseEntity<byte[]> ReadImage(String student_id,String vote_id){
        String img = "img/"+vote_id+"-"+student_id;
        S3Object s3Object = amazonS3Client.getObject(bucket, img);
        String contentType = s3Object.getObjectMetadata().getContentType();
        S3ObjectInputStream stream = s3Object.getObjectContent();

        byte[] imageBytes;
        try {
            imageBytes = IOUtils.toByteArray(stream);
        } catch (IOException e) {
            // 에러 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            IOUtils.closeQuietly(stream); // 스트림 닫기
        }


        // 이미지 바이트 배열을 Response로 반환
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType)) // 이미지 타입에 따라 변경 (JPEG, PNG 등)
                .body(imageBytes);
    }
}


