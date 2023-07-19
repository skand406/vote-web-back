package com.example.votewebback.DTO;

import com.example.votewebback.Entity.VoteEntity;
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
        private String user_id;
        private boolean vote_active;

        public VoteDTO(VoteEntity vote) {
            this.vote_id = vote.getVoteid();
            this.vote_bundle_id = vote.getVotebundleid();
            this.end_date = vote.getEnddate();
            this.vote_name = vote.getVotename();
            this.vote_type = vote.getVotetype();
            this.grade = vote.getGrade();
            this.major = vote.getMajor();
            this.user_id = getUser_id();
            this.vote_active = vote.isVoteactive();
        }
        public String getVoteBundleid() {
            return vote_bundle_id;
        }

        public LocalDate getEndDate() {
            return end_date;
        }
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
        private String student_id;
        private String vote_id;
        private String candidate_spec;
        private String candidate_promise;
    }
}
