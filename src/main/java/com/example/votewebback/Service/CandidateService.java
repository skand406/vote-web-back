package com.example.votewebback.Service;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.example.votewebback.CustomException;
import jakarta.transaction.Transactional;
import org.apache.commons.io.IOUtils;
import com.example.votewebback.DTO.RequestDTO;
import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static com.example.votewebback.Entity.VoteType.PEOPLE;

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
        VoteEntity vote = voteRepository.findByVoteid(requestCandidateDTO.getVote_id()).orElseThrow(()->
                new CustomException(700,"없는 투표 id "+requestCandidateDTO.getVote_id()));

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
    public ResponseDTO.CandidateDTO ReadCandidate(String vote_id, String candidate_id){
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        CandidateEntity candidate = candidateRepository.findByVoteidAndCandidateid(vote, candidate_id).get();
        ResponseDTO.CandidateDTO responseCandidateDTO = new ResponseDTO.CandidateDTO(candidate);
        return responseCandidateDTO;
    }
    @Transactional
    public ResponseDTO.CandidateDTO UpdateCandidate(RequestDTO.CandidateDTO requestCandidateDTO, String vote_id, String candidate_id) throws CustomException {
        CandidateEntity candidate = candidateRepository.findByVoteidAndCandidateid(voteRepository.findByVoteid(vote_id).get(), candidate_id)
                .orElseThrow(() -> new CustomException(711, "존재하지 않는 후보입니다.투표 id와 후보 id를 확인해주세요.") )
                .toBuilder()
                .candidatespec(requestCandidateDTO.getCandidate_spec())
                .candidatepromise(requestCandidateDTO.getCandidate_promise())
                .build();
        candidateRepository.save(candidate);

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

                if (!fileExtension.equalsIgnoreCase("image/png") && !fileExtension.equalsIgnoreCase("image/jpeg")) {
                    throw new CustomException(611,"확장자 오류");
                }

                String fileName= "img/" + vote_id + "-" + candidate_id;
                ObjectMetadata metadata= new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);

            } catch (IOException e) {
                e.printStackTrace();
                Map<Integer,String> error = new HashMap<>();
                error.put(614,"이미지 서버 에러");
                throw new CustomException(614,"이미지 서버 에러");
            } catch (CustomException e) {

                throw new CustomException(e.getErrorCode(),e.getMessage());
            }
        }
        else throw new CustomException(613,"이미지 없음");
    }
    public ResponseEntity<byte[]> ReadImage(String candidate_id, String vote_id) throws CustomException {
        S3ObjectInputStream stream = null; // 여기서 stream 선언
        try {
            String img = "img/" + vote_id + "-" + candidate_id;
            S3Object s3Object = amazonS3Client.getObject(bucket, img);
            String contentType = s3Object.getObjectMetadata().getContentType();
            stream = s3Object.getObjectContent();

            byte[] imageBytes = IOUtils.toByteArray(stream);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType)) // 이미지 타입에 따라 변경 (JPEG, PNG 등)
                    .body(imageBytes);
        } catch (IOException e) {
            // 에러 처리
            e.printStackTrace();
            throw new CustomException(614, "이미지 서버 에러");
        } catch (AmazonS3Exception e) {
            throw new CustomException(688, e + ":사진이 존재하지 않음");
        } finally {
            if (stream != null) {
                IOUtils.closeQuietly(stream); // 스트림 닫기
            }
        }
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


                if (!fileExtension.equalsIgnoreCase("image/png") && !fileExtension.equalsIgnoreCase("image/jpeg")) {
                    throw new CustomException(611,"확장자 오류");
                }
                String fileName= "img/" + vote_id + "-" + candidate_id;
                amazonS3Client.deleteObject(bucket, fileName);

                ObjectMetadata metadata= new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);


            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException(614,"이미지 서버 에러");
            } catch (CustomException e) {
                throw new CustomException(e.getErrorCode(),e.getMessage());
            }
        } else throw new CustomException(613,"이미지 없음");
    }
    public ResponseDTO.StudentDTO IsPeopleVote(RequestDTO.CandidateDTO requestCandidateDTO) throws CustomException {
        VoteEntity vote = voteRepository.findByVoteid(requestCandidateDTO.getVote_id()).get();

        if (vote.getVotetype() == PEOPLE) {
            StudentEntity student = studentRepository.findByStudentid(requestCandidateDTO.getCandidate_id()).orElseThrow(()->
                    new CustomException(602,"투표 타입과 후보 타입이 맞지 않음  "+vote.getVotetype()));
            return new ResponseDTO.StudentDTO(student);
        }
        else return null;
    }
    public void DeleteCandidate(String vote_id, String candidate_id) throws CustomException {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        CandidateEntity candidate = candidateRepository.findByVoteidAndCandidateid(vote,candidate_id).orElseThrow(()->
                new CustomException(700,"없는 후보 "+candidate_id));
        candidateRepository.delete(candidate);

        String fileNameToDelete = "img/" + vote_id + "-" + candidate_id;
        amazonS3Client.deleteObject(bucket, fileNameToDelete);
    }

    public List<CandidateEntity> ReadCandidateCount(String vote_id) throws CustomException {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        if(vote.getEnddate().isBefore(LocalDate.now())){
            throw new CustomException(661,"종료되지 않은 투표");
        } else if (!vote.isVoteactive()) {
            throw new CustomException(673,"사용할 수 없는 투표");
        }
        List<CandidateEntity> candidateList = candidateRepository.findByVoteid(vote);
        candidateList.sort(Comparator.comparing(CandidateEntity::getCandidatecounter).reversed());
        List<CandidateEntity> electedCandidateList = new ArrayList<>();
        int max = candidateList.get(0).getCandidatecounter();
        for(CandidateEntity c : candidateList){
            if(c.getCandidatecounter()==max) electedCandidateList.add(c);
        }
        return electedCandidateList;
    }
}


