package com.example.votewebback.Service;

import com.example.votewebback.CustomException;
import com.example.votewebback.Entity.CandidateEntity;
import com.example.votewebback.Entity.StudentEntity;
import com.example.votewebback.Entity.VoteEntity;
import com.example.votewebback.Entity.VoteType;
import com.example.votewebback.Repository.CandidateRepository;
import com.example.votewebback.Repository.StudentRepository;
import com.example.votewebback.Repository.VoteRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateService candidateService;
    private final StudentRepository studentRepository;

    // 이메일 인증 발송
    public String sendMail(String userEmail, String code, String type) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("webmaster.chlxx00@gmail.com");
        message.setTo(userEmail);
        if(type.equals("pw")) {
            message.setSubject("[Vote Web] 임시 비밀번호 발송 메일");
            message.setText("임시 비밀번호: " + code);
        }
        else{
            message.setSubject("[Vote Web] 이메일 인증 코드 메일");
            message.setText("인증번호: " + code);
        }

        emailSender.send(message);
        return "전송완료";
    }
    public String sendVoteMail(String vote_bundle_id, String userEmail) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, "utf-8");
        VoteEntity vote = voteRepository.findByVotebundleid(vote_bundle_id).get(0);

        mail.setFrom("webmaster.chlxx00@gmail.com");//보내는 사람
        mail.setTo(userEmail);//받는 사람


        mail.setSubject("[Vote Web]" + vote.getVotename() + " 투표 링크 안내");

        // 여기서 원하는 투표 URL을 설정하세요.
        String voteUrl = "http://vote-web.duckdns.org/user/"+vote_bundle_id; // 예시
        String htmlMsg = "<h3>투표에 참여해 주세요!</h3><br>"
                + "<a href='" + voteUrl + "'>"+voteUrl+"</a> 투표 링크를 클릭하여 투표에 참여하세요.";

        mail.setText(htmlMsg, true);


        emailSender.send(mimeMessage);
        return "전송완료";
    }
    public String sendVoteResultMail(String vote_id, String userEmail) throws CustomException, MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, true,"utf-8");
        VoteEntity vote = voteRepository.findByVoteid(vote_id).get();
        List<CandidateEntity> candidateList = candidateService.ReadCandidateCount(vote_id);
        String msg = "";
        for(CandidateEntity candidate:candidateList) {
            //CandidateEntity candidate = candidateRepository.findByVoteidAndCandidateid(vote,candidate.).get();
            String spec = null;
            String promise = null;
            ResponseEntity<byte[]> imageBytes = null;
            if (vote.getVotetype().equals(VoteType.PEOPLE)) { //인물 투표
                StudentEntity student = studentRepository.findByStudentid(candidate.getCandidateid()).get();
                imageBytes = candidateService.ReadImage(student.getStudentid(), vote_id);
                spec = student.getStudentmajor() + student.getStudentgrade() + "학년\n" + student.getStudentname();
                promise = candidate.getCandidatepromise();

            } else if (vote.getVotetype().equals(VoteType.ITEM)) { //사물 투표
                imageBytes = candidateService.ReadImage(candidate.getCandidateid(), vote_id);
                spec = candidate.getCandidatespec();
                promise = candidate.getCandidatepromise();

            } else { //찬반 투표

                spec = candidate.getCandidatespec();
                promise = candidate.getCandidatepromise();
            }

            // 이미지를 이메일에 첨부합니다.
            mail.addInline("image", new ByteArrayDataSource(imageBytes.getBody(), imageBytes.getHeaders().getContentType().toString()));

            mail.setFrom("webmaster.chlxx00@gmail.com");
            mail.setTo(userEmail);
            mail.setSubject("[Vote Web]" + vote.getVotename() + " 투표 결과 안내");

            // HTML 본문
            String htmlMsg = "<table border='1' style='width: 300px;'>"
                    + "<thead>"
                    + "    <tr>"
                    + "        <th style='width: 100px;'><img src='cid:image' alt='이미지 아이콘' width='50'> </th>"
                    + "        <th style='width: 200px;'>spec</th>"
                    + "    </tr>"
                    + "</thead>"
                    + "<tbody>"
                    + "    <tr>"
                    + "        <td style='width: 100px;'>promise</td>"
                    + "        <td style='width: 200px;'></td>"
                    + "    </tr>"
                    + "</tbody>"
                    + "</table>";


            htmlMsg = htmlMsg.replace("spec", spec).replace("promise", promise);
            msg+=htmlMsg;
        }
        mail.setText(msg, true);

        emailSender.send(mimeMessage);
        return "전송완료";
    }

}