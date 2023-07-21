package com.example.votewebback.Repository;

import com.example.votewebback.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity,String> {
    List<StudentEntity> findByStudentgrade(int student_grade);
    List<StudentEntity> findByStudentmajor(String student_major);
    List<StudentEntity> findByStudentgradeAndStudentmajor(int student_grade, String student_major);
    StudentEntity findByStudentid(String student_id);
}
