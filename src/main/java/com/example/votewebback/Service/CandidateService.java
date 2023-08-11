package com.example.votewebback.Service;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.example.votewebback.CustomException;
import org.apache.commons.io.IOUtils;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.example.votewebback.Entity.VoteType.PEOPLE;

@Service
@RequiredArgsConstructor
public class CandidateService {

    @Autowired
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;
    private final StudentRepository studentRepository;
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<ResponseDTO.CandidateDTO> ReadCandidateListByVoteId(String vote_id){
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        List<CandidateEntity> candidateList = candidateRepository.findByVoteid(vote);
        List<ResponseDTO.CandidateDTO> responseCandidateList = new ArrayList<>();

        for(CandidateEntity candidate : candidateList){
            ResponseDTO.CandidateDTO responseCandidate = new ResponseDTO.CandidateDTO(candidate);
            responseCandidateList.add(responseCandidate);
        }

        return responseCandidateList;
    }
    public ResponseDTO.CandidateDTO  CreateCandidate(RequestDTO.CandidateDTO requestCandidateDTO) throws CustomException {
        String imgPath = (requestCandidateDTO.getVote_id()) + "-" + (requestCandidateDTO.getCandidate_id());
        VoteEntity vote = voteRepository.findByVoteid(requestCandidateDTO.getVote_id()).get();
        if (vote == null) {
            Map<Integer,String> error = new HashMap<>();
            error.put(700,"사용할 수 없는 투표 id : " + requestCandidateDTO.getVote_id());
            throw new CustomException(error);
            // 또는 원하는 예외 타입을 사용하여 처리할 수 있습니다.
        }
        else {
            CandidateEntity candidate = CandidateEntity.builder()
                    .candidateid(requestCandidateDTO.getCandidate_id())
                    .voteid(vote)
                    .candidatespec(requestCandidateDTO.getCandidate_spec())
                    .candidatepromise(requestCandidateDTO.getCandidate_promise())
                    .candidatecounter(0) // 득표수 0
                    .imgpath(imgPath) // img 이름g
                    .build();

            candidateRepository.save(candidate);
            ResponseDTO.CandidateDTO responseCandidateDTO = new ResponseDTO.CandidateDTO(candidate);
            return responseCandidateDTO;
        }
    }
    public ResponseDTO.CandidateDTO SearchCandidate(String vote_id, String candidate_id){
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        CandidateEntity candidate = candidateRepository.findByVoteidAndCandidateid(vote, candidate_id).get();
        ResponseDTO.CandidateDTO responseCandidateDTO = new ResponseDTO.CandidateDTO(candidate);
        return responseCandidateDTO;
    }
    public void CreateImage(@NotNull MultipartFile file, String vote_id, String candidate_id) throws IOException, CustomException {
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
                        Map<Integer,String> error = new HashMap<>();
                        error.put(611,"확장자 오류");
                        throw new CustomException(error);
                    }

                    String fileName= "img/" + vote_id + "-" + candidate_id;
                    ObjectMetadata metadata= new ObjectMetadata();
                    metadata.setContentType(file.getContentType());
                    metadata.setContentLength(file.getSize());
                    amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);

                } else {
                    Map<Integer,String> error = new HashMap<>();
                    error.put(612,"크기 오류 현재 크기 : "+image.getHeight() +"/"+ image.getWidth());
                    throw new CustomException(error);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Map<Integer,String> error = new HashMap<>();
                error.put(614,"이미지 서버 에러");
                throw new CustomException(error);
            } catch (CustomException e) {
                Map<Integer, String> error = e.getError();
                throw new CustomException(error);
            }
        }
        Map<Integer, String> error = new HashMap<>();
        error.put(613,"이미지 없음");
        throw new CustomException(error);
    }
    public ResponseEntity<byte[]> ReadImage(String candidate_id, String vote_id){
        String img = "img/"+vote_id+"-"+ candidate_id;
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
    public void UpdateImage(MultipartFile file, String vote_id, String candidate_id) throws IOException, CustomException {
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
                        Map<Integer,String> error = new HashMap<>();
                        error.put(611,"확장자 오류");
                        throw new CustomException(error);
                    }
                    String fileName= "img/" + vote_id + "-" + candidate_id;
                    amazonS3Client.deleteObject(bucket, fileName);

                    ObjectMetadata metadata= new ObjectMetadata();
                    metadata.setContentType(file.getContentType());
                    metadata.setContentLength(file.getSize());
                    amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);


                } else {
                    Map<Integer, String> error = new HashMap<>();
                    error.put(612, "크기 오류 현재 크기 : " + image.getHeight() + "/" + image.getWidth());
                    throw new CustomException(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Map<Integer,String> error = new HashMap<>();
            error.put(614,"이미지 서버 에러");
            throw new CustomException(error);
        } catch (CustomException e) {
            Map<Integer, String> error = e.getError();
            throw new CustomException(error);
        }
        Map<Integer, String> error = new HashMap<>();
        error.put(613,"이미지 없음");
        throw new CustomException(error);
        }
    }

    public ResponseDTO.StudentDTO IsPeopleVote(RequestDTO.CandidateDTO requestCandidateDTO) {
        VoteEntity vote = voteRepository.findByVoteid(requestCandidateDTO.getVote_id()).get();

        if (vote.getVotetype() == PEOPLE) {
            StudentEntity student = studentRepository.findByStudentid(requestCandidateDTO.getCandidate_id());
            return new ResponseDTO.StudentDTO(student);
        }
        else return null;
    }

    public void DeleteCandidate(String vote_id, String candidate_id) {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        CandidateEntity candidate = candidateRepository.findByVoteidAndCandidateid(vote,candidate_id).orElseThrow(()->
                new IllformedLocaleException("없는 후보 "+candidate_id));
        candidateRepository.delete(candidate);

        String fileNameToDelete = "img/" + vote_id + "-" + candidate_id;
        amazonS3Client.deleteObject(bucket, fileNameToDelete);
    }
}


