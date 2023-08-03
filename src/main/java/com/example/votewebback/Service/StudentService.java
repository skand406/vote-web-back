package com.example.votewebback.Service;

import com.example.votewebback.DTO.ResponseDTO;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<ResponseDTO.StudentDTO> ReadStudentList(){
        List<StudentEntity> studentEntityList = studentRepository.findAll();
        List<ResponseDTO.StudentDTO> responseVoteList = new ArrayList<>();

        for(StudentEntity student : studentEntityList){
            ResponseDTO.StudentDTO responseVote = new ResponseDTO.StudentDTO(student);
            responseVoteList.add(responseVote);
        }
        return responseVoteList;
    }

}
