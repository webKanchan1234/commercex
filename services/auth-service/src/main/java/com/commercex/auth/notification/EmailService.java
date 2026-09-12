package com.commercex.auth.notification;

import com.commercex.auth.entity.User;

public interface EmailService {

    void sendVerificationEmail(
            User user,
            String token
    );

    void sendPasswordResetEmail(
            User user,
            String token
    );

}
