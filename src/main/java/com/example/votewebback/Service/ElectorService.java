package com.example.votewebback.Service;

import com.example.votewebback.Entity.ElectorEntity;
import com.example.votewebback.Entity.StudentEntity;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.RandomCode;
import com.example.votewebback.Repository.ElectorRepository;
import com.example.votewebback.Repository.StudentRepository;
import com.example.votewebback.Repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ElectorService {

    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final ElectorRepository electorRepository;
    @Autowired
    private final VoteRepository voteRepository;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final RedisService redisService;

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
                ElectorEntity elector = ElectorEntity.builder()
                        .studentid(studentRepository.findByStudentid(student.getStudentid()))
                        .voteid(voteRepository.findByVoteid(vote_id))
                        .voteconfirm(false)
                        .build();
                electorRepository.save(elector);
            }
    }
    public String CheckElectorAuthority(String  vote_id, String student_id, String email){
        VoteEntity vote=voteRepository.findByVoteid(vote_id);
        StudentEntity student=studentRepository.findByStudentid(student_id);
        Optional<ElectorEntity> elector = electorRepository.findByVoteidAndStudentid(vote,student);
        if(!elector.isEmpty()){
            if(email.equals(student.getStudentemail())){
                String code = RandomCode.randomCode();
                System.out.println(code); //나중에 수정 이메일 들어가기 싫어서 작성함
                redisService.setDataExpire(code, email, 60 * 5L);
                return emailService.sendMail(email,code,"elector");
            }
            else return "이메일이 일치하지 않습니다.";
        }
        else return "권한이 없는 유권자입니다.";
    }
}


//- 대상자 추가 필요(학생 리스트 ← 학년, 전공 등 조건으로 권한 부여 대상 추리기)
//        - ElectorService : CreateElector 필요
//        - 투표 대상자 확인하는 함수
//        - 컨트롤러에서 createVote 함수와 함께 사용
//
//        - 버전1의 UserService : 대상자 비교(create_user 참고)