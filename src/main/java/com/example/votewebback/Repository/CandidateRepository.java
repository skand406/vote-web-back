package com.example.votewebback.Repository;

import com.example.votewebback.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, VoteStudentIdKey> {
   CandidateEntity findByVoteidAndStudentid(VoteEntity vote_id, StudentEntity student_id);
   List<CandidateEntity> findByVoteid(VoteEntity vote_id);
}
