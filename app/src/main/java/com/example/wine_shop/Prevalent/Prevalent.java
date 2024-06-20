package com.example.wine_shop.Prevalent;

import com.example.wine_shop.Model.Users;

public class Prevalent {

    public static Users currentOnlineUser;

    public static final String USER_NAME_KEY = "userName";
    public static final String USER_PASSWORD_KEY = "userPassword";

    public static Users getCurrentOnlineUser() {
        return currentOnlineUser;
    }

    public static void setCurrentOnlineUser(Users currentOnlineUser) {
        Prevalent.currentOnlineUser = currentOnlineUser;
    }
}
