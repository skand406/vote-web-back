package com.example.votewebback.Repository;

import com.example.votewebback.Entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<VoteEntity, String> {

    //List<VoteEntity> findByUserid(String user_id);

}
