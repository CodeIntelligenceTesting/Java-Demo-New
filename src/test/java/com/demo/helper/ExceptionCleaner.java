package com.demo.helper;

/**
 * Exception helper class that provides utility for cleaner fuzzing setup.
 */
public class ExceptionCleaner {
    /**
     * helper function that suppresses exceptions that are being thrown by mockMVC
     * @param e exception to check if in need of suppression
     */
    public static void cleanException(IllegalArgumentException e) {
        if (e.getMessage().contains("is not a valid URI")) {
            return;
        } else if (e.getMessage().contains("Not enough variable values available to expand")){
            return;
        }
        throw e;
    }
}
