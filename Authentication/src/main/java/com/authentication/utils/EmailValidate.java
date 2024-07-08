package com.authentication.utils;

import java.util.regex.Pattern;

public class EmailValidate {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    public static boolean isValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
