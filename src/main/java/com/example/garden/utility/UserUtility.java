package com.example.garden.utility;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtility {

    public static String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
