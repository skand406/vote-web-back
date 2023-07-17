package com.example.votewebback.Repository;

import com.example.votewebback.DTO.UserDTO;
import com.example.votewebback.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    //UserEntity findByUserid(String manager_id);


    UserDTO findByUserid(String user_id);
}
