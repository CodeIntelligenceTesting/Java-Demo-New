package com.demo.security;

import com.demo.security.helper.DBMock;
import com.demo.security.helper.JsonAddBody;
import com.demo.security.helper.UserJSONObject;
import com.demo.security.helper.UserObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

@RestController()
public class ComplexSecurityExample {

    @PostMapping("/add")
    public void addNewUser(@RequestBody JsonAddBody jsonAddBody) {
        Base64.Encoder base64 = Base64.getEncoder();
        if (base64.encodeToString(jsonAddBody.getAuthenticatedAdminUser().getUsername().getBytes()).equals("YWRtaW46")
            && (jsonAddBody.getAuthenticatedAdminUser().getChallenge()
                ^ jsonAddBody.getAuthenticatedAdminUser().getPubKey())
                == jsonAddBody.getAuthenticatedAdminUser().getPubKey()) {

            UserObject newUserObject = fromJSON(jsonAddBody.getNewUser());
            DBMock.saveUserObject(newUserObject);
        }

    }

    public static UserObject fromJSON(UserJSONObject jsonObject){
        UserObject userObject = UserObject.fromJSON(jsonObject);

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(jsonObject.getSerializedGreeter()));
            userObject.setGreeting((UserObject.IndividualGreeting) ois.readObject());
        } catch (Exception ignored) {}


        return userObject;
    }
}
