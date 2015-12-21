package io.github.fanky10.sociallogin.module.utils;

/**
 * Created by mariano.salvetti on 21/12/2015
 * Two method for validate the information in the register screen.
 * TODO: Change for your own logic.
 */
public class ValidateUserInfo {

    public static boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPasswordValid(String password) {
        final int MIN_LENGTH_PASSWORD = 4;
        return password.length() > MIN_LENGTH_PASSWORD;
    }
}
