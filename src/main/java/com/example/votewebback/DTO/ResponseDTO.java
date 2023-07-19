package com.example.votewebback.DTO;

import com.example.votewebback.Entity.StudentEntity;
import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Entity.VoteEntity;
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

        UserEntity userEntity;

        public VoteDTO() {

        }

        public VoteDTO(VoteEntity vote) {
            this.vote_id = vote.getVoteid();
            this.vote_bundle_id = vote.getVotebundleid();
            this.start_date = vote.getStartdate();
            this.end_date = vote.getEnddate();
            this.last_end_date = vote.getLastenddate();
            this.vote_name = vote.getVotename();
            this.vote_type = vote.getVotetype();
            this.grade = vote.getGrade();
            this.major = vote.getMajor();
            this.user_id = userEntity.getUserid();
        }
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

        public StudentDTO(StudentEntity student) {
            this.student_id = student.getStudentid();
            this.student_name = student.getStudentname();
            this.student_grade = student.getStudentgrade();
            this.student_major = student.getStudentmajor();
        }
    }
}
