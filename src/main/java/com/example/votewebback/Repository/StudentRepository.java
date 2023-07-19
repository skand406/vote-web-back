package com.example.votewebback.Repository;

import com.example.votewebback.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity,String> {

    //List<StudentEntity> findByUserid(String user_id);

}
