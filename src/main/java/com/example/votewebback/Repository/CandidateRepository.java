package com.example.votewebback.Repository;

import com.example.votewebback.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, VoteCandidateIdKey> {
   CandidateEntity findByVoteidAndCandidateid(VoteEntity vote_id, String candidate_id);
   List<CandidateEntity> findByVoteid(VoteEntity vote_id);
}
