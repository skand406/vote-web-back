package com.example.votewebback.Repository;

import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.Entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String> {

    List<VoteEntity> findByUserid(UserEntity user_id);
    VoteEntity findByVoteid(String vote_id);
    List<VoteEntity> findByVotebundleid(String vote_bundle_id);

}
