package com.nghiasoftware.bookshop_consumer_email_service.consumer;

import com.google.gson.Gson;
import com.nghiasoftware.bookshop_consumer_email_service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmail {

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = "email", groupId = "email-service-group")
    public void sendEmail(String userDTO) {
        Gson gson = new Gson();
        UserDTO user = gson.fromJson(userDTO, UserDTO.class);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to Bookshop");
        message.setText("This your password: " + user.getPassword());

        try {
            javaMailSender.send(message);
            System.out.println("Email sent successfully to " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Error sending email to " + user.getEmail() + ": " + e.getMessage());
        }
    }

}
