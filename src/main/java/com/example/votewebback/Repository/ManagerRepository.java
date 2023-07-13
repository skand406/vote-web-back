package com.example.votewebback.Repository;

import com.example.votewebback.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity,String> {

    ManagerEntity findByManagerid(String manager_id);

    @Override
    Optional<ManagerEntity> findById(String manager_id);
}
