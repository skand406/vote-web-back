package com.example.votewebback.DTO;

import com.example.votewebback.Entity.VoteType;
import lombok.Getter;

import java.time.LocalDate;

public class ResponseDTO {
    @Getter
    public static class VoteDTO {
        private String vote_id;
        private String vote_bundle_id;
        private LocalDate start_date;
        private LocalDate end_date;
        private LocalDate last_end_date;
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
    }

    @Getter
    public static class CandidateDTO {
        private String student_id;
        private String vote_id;
        private String candidate_spec;
        private String candidate_promise;
        private String img_path;
    }

    @Getter
    public static class StudentDTO{
        private String student_id;
        private String student_name;
        private int student_grade;
        private String student_major;
    }
}
