package com.example.votewebback.Repository;

import com.example.votewebback.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,String> {
    List<StudentEntity> findByStudentgrade(int student_grade);
    List<StudentEntity> findByStudentmajor(String student_major);
    List<StudentEntity> findByStudentgradeAndStudentmajor(int student_grade, String student_major);
    Optional<StudentEntity> findByStudentid(String student_id);
}
