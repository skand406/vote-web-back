package com.example.votewebback.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

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

}