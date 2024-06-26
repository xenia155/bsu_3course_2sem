package by.quantumquartet.quanthink.services;

import static by.quantumquartet.quanthink.services.AppLogger.logInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendSimpleEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Complete registration!");
        message.setText("To confirm your account, please click here: "
                + "http://localhost:8080/confirmAccount?token=" + token);
        javaMailSender.send(message);
        logInfo(EmailService.class, "Account confirmation message sended to " + email);
    }
}
