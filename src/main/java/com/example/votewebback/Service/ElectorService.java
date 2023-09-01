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

import java.util.*;

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
                        .studentid(studentRepository.findByStudentid(student.getStudentid()).get())
                        .voteid(voteRepository.findByVoteid(vote_id).get())
                        .voteconfirm(false)
                        .build();
                electorRepository.save(elector);
            }
    }
    public String CheckElectorAuthority(String  vote_id, String student_id, String email){
        VoteEntity vote=voteRepository.findByVoteid(vote_id).get();
        StudentEntity student=studentRepository.findByStudentid(student_id).get();
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

    public void DeleteElector(String vote_id, StudentEntity student_id) {
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        ElectorEntity elector = electorRepository.findByVoteidAndStudentid(vote,student_id).orElseThrow(()->
                new IllformedLocaleException("없는 유권자"+student_id));
        electorRepository.delete(elector);

    }

    public Map<String, Object> ReadParticipationRate(String vote_id){

        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        List<ElectorEntity> electorList = electorRepository.findByVoteid(vote);

        //전체 유권자
        int AllElector = electorList.size();
        //전체 유권자 중 참여한 사람
        int participation=0;
        Map <String,Object> result = new HashMap<>();
        Map<String, Integer> major = new HashMap<>() {{
            put("소프트웨어융합공학과", 0);
            put("메카트로닉스공학과", 0);
            put("금융투자학과", 0);
            put("산업경영학과", 0);
        }};
        Map<String,Integer> grade = new HashMap<>(){{
            put("1학년", 0);
            put("2학년", 0);
            put("3학년", 0);
            put("4학년", 0);
        }};

        for(ElectorEntity e: electorList){
            //전체 참여자
            if(e.isVoteconfirm()){
                String student_id = e.getStudentid().getStudentid();
                StudentEntity student = studentRepository.findByStudentid(student_id).get();

                String m = student.getStudentmajor();
                String g = student.getStudentgrade()+"학년";
                // 키가 같으면 밸류 대체
                major.put(m, major.get(m) + 1);
                grade.put(g, grade.get(g) + 1);
                participation++; //투표한 사람

            }
        }
        result.put("participants",participation);
        result.put("total",AllElector);
        //밸류 0이면 키 삭제
        major.values().removeIf(value -> value == 0);
        result.put("major",major);
        grade.values().removeIf(value -> value == 0);
        result.put("grade",grade);
//        for(Map.Entry<String,Integer> map:count.entrySet()){
//            System.out.println(map.getKey()+":"+map.getValue());
//        }
//
//        System.out.println("참여자"+participation);
//        System.out.println("전체"+AllElector);

        return result;
    }
}