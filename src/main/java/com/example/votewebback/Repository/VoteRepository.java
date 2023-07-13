package com.example.votewebback.Repository;

import com.example.votewebback.Entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<VoteEntity, String> {
}
