package com.demo.handler;

import com.demo.dto.UserDTO;
import com.demo.helper.DatabaseMock;

import java.util.Collection;

/**
 * Handler class that provides functionality for the controller class.
 */
public class UserHandler {

    private static final DatabaseMock db = DatabaseMock.getInstance();

    private static void dbInitCheck(){
        if (db.isInitialized()) {
            db.init();
        }
    }

    public static Collection<UserDTO> returnUsers(){
        dbInitCheck();
        return db.getAllUsers();
    }

    public static UserDTO returnSpecificUser(String id) {
        dbInitCheck();
        return db.getUserWithId(id);
    }

    public static boolean deleteUser(String id) {
        dbInitCheck();
        return db.deleteUser(id);
    }

    public static String createUser(UserDTO userDTO){
        dbInitCheck();
        return updateUser(userDTO, db.getNextFreeCategoryId());
    }

    public static String updateUser(UserDTO userDTO, String id) {
        dbInitCheck();
        db.putUser(id, userDTO);
        return id;
    }
}
