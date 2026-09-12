package com.commercex.auth.audit;

import com.commercex.auth.entity.User;

public interface SecurityAuditService{

    void loginSuccess(User user);

    void loginFailed(String email);

    void logout(User user);

    void refresh(User user);

    void passwordChanged(User user);

    void tokenReuse(User user);

}