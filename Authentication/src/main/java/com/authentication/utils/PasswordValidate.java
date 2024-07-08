package com.authentication.utils;

public class PasswordValidate {
    public static boolean isValid(String password) {
        if (password.length() < 8) return false;
        boolean hasLetter = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasLetter && hasDigit && hasSpecial;
    }
}
