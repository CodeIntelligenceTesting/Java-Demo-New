package com.demo.helper;

import org.springframework.http.HttpStatus;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

public class CustomMatchers extends StatusResultMatchers {
    public static ResultMatcher isNot5xxServerError() {
        return (result) -> {
            int status = result.getResponse().getStatus();
            AssertionErrors.assertTrue("Expect status not to be 5xx. Status is " + status,
                    (status >= 200 && status < 300) || (status >= 400 && status < 500));
        };
    }
}
