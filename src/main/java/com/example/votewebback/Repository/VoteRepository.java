package com.example.votewebback.Repository;

import com.example.votewebback.Entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String> {

    //List<VoteEntity> findByUserid(String user_id);
    VoteEntity findByVoteid(String vote_id);

}
