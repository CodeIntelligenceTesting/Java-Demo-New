package com.demo.helper;

public class ExceptionCleaner {

    public static void cleanException(IllegalArgumentException e) {
        if (e.getMessage().contains("is not a valid URI")) {
            return;
        } else if (e.getMessage().contains("Not enough variable values available to expand")){
            return;
        }
        throw e;
    }
}
