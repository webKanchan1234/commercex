package com.commercex.auth.notification;

import com.commercex.auth.entity.User;
import com.commercex.auth.entity.UserSession;
import org.springframework.stereotype.Service;

@Service
public class LoginNotificationServiceImpl
        implements LoginNotificationService{

    @Override
    public void notifyLogin(
            User user,
            UserSession session){

        System.out.println(

                "New Login : "

                        +

                        session.getBrowser()

                        +

                        " "

                        +

                        session.getIpAddress()

        );

    }

}
