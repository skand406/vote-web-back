package com.example.votewebback.Service;

import com.example.votewebback.Entity.ElectorEntity;
import com.example.votewebback.Entity.StudentEntity;
import com.example.votewebback.Repository.ElectorRepository;
import com.example.votewebback.Repository.StudentRepository;
import com.example.votewebback.Repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectorService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ElectorRepository electorRepository;
    @Autowired
    private VoteRepository voteRepository;

    public void CreateElector(String major,int grade, String vote_id) {
            List<StudentEntity> studentList;

            if (major.equals("모든과") && grade == 0)
                studentList = studentRepository.findAll();
            else if (major.equals("모든과") && grade > 0)
                studentList = studentRepository.findByStudentgrade(grade);
            else if (!major.equals("모든과") && grade == 0)
                studentList = studentRepository.findByStudentmajor(major);
            else
                studentList = studentRepository.findByStudentgradeAndStudentmajor(grade, major);

            for (StudentEntity student : studentList) {
                ElectorEntity elector = new ElectorEntity();
                elector.setStudentid(studentRepository.findByStudentid(student.getStudentid()));
                elector.setVoteid(voteRepository.findByVoteid(vote_id));
                electorRepository.save(elector);
            }
    }
}


//- 대상자 추가 필요(학생 리스트 ← 학년, 전공 등 조건으로 권한 부여 대상 추리기)
//        - ElectorService : CreateElector 필요
//        - 투표 대상자 확인하는 함수
//        - 컨트롤러에서 createVote 함수와 함께 사용
//
//        - 버전1의 UserService : 대상자 비교(create_user 참고)