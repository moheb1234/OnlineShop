package com.example.onlineshop.service;

import com.example.onlineshop.ex_handler.ExceptionMessage;
import com.example.onlineshop.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import static com.example.onlineshop.ex_handler.ExceptionMessage.USER_NOT_ENABLES;

@Validated
@Service
@RequiredArgsConstructor
public class EmailService {
    @Resource
    private JavaMailSender javaMailSender;

    private final UserService userService;


    public String resendVerifyingEmil(@NotEmpty String email) {
        User user = userService.findByEmail(email);
        if (user.isEnabled()|| user.getVerifyingCode()==null)
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_VERIFIED);
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

    public String sendForgottenPasswordEmail(@NotEmpty String email) {
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
