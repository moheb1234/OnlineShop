package com.example.onlineshop.service;

import com.example.onlineshop.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.example.onlineshop.ex_handler.ExceptionMessage.USER_NOT_ENABLES;

@Service
public class EmailService {
    @Resource
    private JavaMailSender javaMailSender;

    private UserService userService;


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void sendVerifyingEmil(User user) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("verifying emile");
        msg.setText("verifying code: \n" + user.getVerifyingCode());
        javaMailSender.send(msg);
    }

    public String resendVerifyingEmil(User user) {
        int verifyingCode = (int) ((Math.random() * 900000) + 100000);
        user.setVerifyingCode(verifyingCode + "");
        userService.save(user);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("verifying emile");
        msg.setText("verifying code: \n" + user.getVerifyingCode());
        javaMailSender.send(msg);
        return "we resend verifying code to "+user.getEmail()+" ";
    }

    public String sendForgottenPasswordEmail(String email) {
        User user = userService.findByEmail(email);
        if (user.isEnabled()) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Forget Password");
            msg.setText("Hi  " + user.getUsername() + "\n\n your password is: \n" + user.getPassword());
            javaMailSender.send(msg);
            return "We Send Your password in " + email;
        }
        throw new IllegalArgumentException(USER_NOT_ENABLES);
    }
}
