package com.demo.handler;

import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.helper.DatabaseMock;

import java.util.ArrayList;
import java.util.Collection;

public class UserHandler {

    private static final DatabaseMock db = DatabaseMock.getInstance();

    private static void dbInitCheck(){
        if (!db.isInitialized()) {
            db.init();
        }
    }

    /**
     * {@link com.demo.Controller.UserController#getUsers(String)}
     * @return
     */
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
        return updateUser(userDTO, db.getNextFreeCategoryId());
    }

    public static String updateUser(UserDTO userDTO, String id) {
        dbInitCheck();
        db.putUser(id, userDTO);
        return id;
    }
}
