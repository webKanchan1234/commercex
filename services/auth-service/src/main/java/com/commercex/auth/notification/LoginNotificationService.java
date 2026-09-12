package com.commercex.auth.notification;

import com.commercex.auth.entity.User;
import com.commercex.auth.entity.UserSession;

public interface LoginNotificationService {

    void notifyLogin(

            User user,

            UserSession session

    );

}
