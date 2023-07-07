package com.example.votewebback.DTO;

import com.example.votewebback.Entity.VoteType;
import lombok.Getter;

import java.time.LocalDate;

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
        private String manager_id;
        private boolean vote_active;
    }

    @Getter
    public static class ManagerDTO {
        private String manager_name;
        private String manager_tel;
        private String manager_email;
        private String manager_id;
        private String manager_password;
    }

    @Getter
    public static class CandidateDTO {
        private String student_id;
        private String vote_id;
        private String candidate_spec;
        private String candidate_promise;
    }
}
