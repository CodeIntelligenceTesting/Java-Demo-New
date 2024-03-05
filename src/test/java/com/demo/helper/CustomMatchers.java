package com.demo.helper;

import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

/**
 * Helper class that provides custom functions to check the HTTP result codes.
 */
public class CustomMatchers extends StatusResultMatchers {
    /**
     * Helper function that allows all but error 5xx return codes.
     * @return Result matcher object
     */
    public static ResultMatcher isNot5xxServerError() {
        return (result) -> {
            int status = result.getResponse().getStatus();
            AssertionErrors.assertTrue("Expect status not to be 5xx. Status is " + status,
                    (status < 500 || status >= 600));
        };
    }
}
