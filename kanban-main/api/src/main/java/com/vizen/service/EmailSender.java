package com.vizen.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;



@Component
@Log4j2
public class EmailSender  {

    @Value("${EMAIL_FROM:}")
    private String fromEmailAddress;

    @PostConstruct
    public void init() {
        log.info("Going to send email from :" + fromEmailAddress);
    }

    private JavaMailSender emailSender;

    public EmailSender(JavaMailSender emailSender){
        this.emailSender=emailSender;
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {
       try{
           SimpleMailMessage message = new SimpleMailMessage();
           message.setFrom(fromEmailAddress);
           message.setTo(to);
           message.setSubject(subject);
           message.setText(text);
           emailSender.send(message);
       } catch (Exception e){
           log.error("Exception sending  email to: " + to, e);
           e.printStackTrace();
       }

    }
}
