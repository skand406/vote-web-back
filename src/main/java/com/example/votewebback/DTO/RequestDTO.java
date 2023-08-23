package com.example.votewebback.DTO;

import com.example.votewebback.Entity.StudentEntity;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.Entity.VoteType;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;

@Getter
public class RequestDTO {

    @Getter
    public static class VoteDTO {
        private String vote_id;
        private String vote_bundle_id;

        private LocalDate start_date;
        private LocalDate end_date;
        private String vote_name;
        private VoteType vote_type;
        private int grade;
        private String major;
        private String user_id;

    }

    @Getter
    public static class UserDTO {
        private String user_name;
        private String user_tel;
        private String user_email;
        private String user_id;
        private String user_password;
    }
    @Getter
    public static class CandidateDTO {
        private String candidate_id;
        private String vote_id;
        private String candidate_spec;
        private String candidate_promise;
    }
    @Getter
    public static class ImgDTO{
        private String vote_id;
        private String candidate_id;
        private MultipartFile img;
    }
    public record LoginDTO(String user_id, String user_password){}

}
