package com.example.votewebback.DTO;

import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Getter
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
        private boolean vote_active;

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
            UserEntity user = vote.getUserid();
            this.user_id = user.getUserid();
            this.vote_active = vote.isVoteactive();
        }
    }

    @Getter
    public static class UserDTO {
        private String user_name;
        private String user_tel;
        private String user_email;
        private String user_id;

        public UserDTO(UserEntity user) {
            this.user_name = user.getUserName();
            this.user_tel = user.getUsertel();
            this.user_email = user.getUseremail();
            this.user_id = user.getUserid();
        }
    }

    @Getter
    public static class CandidateDTO {
        private String candidate_id;
        private String vote_id;
        private String candidate_spec;
        private String candidate_promise;
        private String img_path;

        public CandidateDTO(CandidateEntity candidate) {
            this.candidate_id = candidate.getCandidateid();
            VoteEntity vote = candidate.getVoteid();
            this.vote_id = vote.getVoteid();
            this.candidate_spec = candidate.getCandidatespec();
            this.candidate_promise = candidate.getCandidatepromise();
            this.img_path = candidate.getImgpath();
        }
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

    public record LoginDTO(String accessToken,String refreshToken){}

}
