package com.example.parisjanitormsuser.service.impl;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final int MIN_PASSWORD_LENGTH = 2;

    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean isFieldNotEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }

    public static boolean isRegistrationValid(String email, String password, String username) {
        return isEmailValid(email) && isPasswordValid(password) && isFieldNotEmpty(username);
    }

    public static boolean isLoginValid(String email, String password) {
        return isEmailValid(email) && isPasswordValid(password);

    }

}
