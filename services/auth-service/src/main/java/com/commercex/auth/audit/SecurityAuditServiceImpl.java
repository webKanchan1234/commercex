package com.commercex.auth.audit;

import com.commercex.auth.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecurityAuditServiceImpl
        implements SecurityAuditService{

    @Override
    public void loginSuccess(User user){

        log.info(

                "LOGIN SUCCESS {}",

                user.getEmail()

        );

    }

    @Override
    public void loginFailed(String email) {

    }

    @Override
    public void logout(User user) {

    }

    @Override
    public void refresh(User user) {

    }

    @Override
    public void passwordChanged(User user) {

    }

    @Override
    public void tokenReuse(User user) {

    }

}
