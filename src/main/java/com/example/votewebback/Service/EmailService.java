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
    public String sendMail(String userEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("webmaster.chlxx00@gmail.com");
        message.setTo(userEmail);
        message.setSubject("[Vote Web] 이메일 인증 코드 메일");
        message.setText("인증번호: " + code);

        emailSender.send(message);
        return "전송완료";
    }

}