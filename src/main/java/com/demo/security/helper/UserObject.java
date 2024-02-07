package com.demo.security.helper;

import java.io.Serializable;

public class UserObject {

    public static class IndividualGreeting implements Serializable {
        private static final long serialVersionUID = 123456789L;
    }
    private String username;
    private String clearName;
    private IndividualGreeting greeting;


    public static UserObject fromJSON(UserJSONObject jsonObject) {
        UserObject userObject = new UserObject();
        userObject.username = jsonObject.getUsername();
        userObject.clearName = jsonObject.getClearName();

        return userObject;
    }

    public void setGreeting(IndividualGreeting greeting) {
        this.greeting = greeting;
    }


}
