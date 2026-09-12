package com.commercex.auth.notification;

import com.commercex.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl
        implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(
            User user,
            String token
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(user.getEmail());

        message.setSubject(
                "Verify your CommerceX account"
        );

        message.setText(

                "Click below to verify your account:\n\n"

                        +

                        "http://localhost:8081/api/auth/verify?token="

                        +

                        token

        );

        mailSender.send(message);

    }

    @Override
    public void sendPasswordResetEmail(
            User user,
            String token
    ){

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(user.getEmail());

        message.setSubject(
                "Reset Your Password"
        );

        message.setText(

                "Click below to reset your password:\n\n"

                        +

                        "http://localhost:3000/reset-password?token="

                        +

                        token

        );

        mailSender.send(message);

    }

}
