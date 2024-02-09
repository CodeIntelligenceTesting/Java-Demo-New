package com.demo.handler;

import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.helper.DatabaseMock;

import java.util.ArrayList;
import java.util.Collection;

public class CarCategoryHandler {

    private static final DatabaseMock db = DatabaseMock.getInstance();

    private static void dbInitCheck(){
        if (!db.isInitialized()) {
            db.init();
        }
    }

    public static Collection<CarCategoryDTO> returnVIPCategories(){
        Collection<CarCategoryDTO> vipCategories = new ArrayList<>();
        for (CarCategoryDTO category : db.getAllCategories()) {
            if (category.getVisibleTo() == UserDTO.Role.VIP_USER) {
                vipCategories.add(category);
            }
        }

        return vipCategories;
    }
    public static Collection<CarCategoryDTO> returnDefaultCategories(){
        Collection<CarCategoryDTO> defaultCategories = new ArrayList<>();
        dbInitCheck();
        for (CarCategoryDTO category : db.getAllCategories()) {
            if (category.getVisibleTo() == UserDTO.Role.DEFAULT_USER) {
                defaultCategories.add(category);
            }
        }
        return defaultCategories;
    }

    public static CarCategoryDTO returnSpecificCategory(String id) {
        dbInitCheck();
        return db.getCategoryWithId(id);
    }

    public static boolean deleteCategory(String id) {
        dbInitCheck();
        return db.deleteCategory(id);
    }

    public static String createCategory(CarCategoryDTO categoryDTO){
        return updateCategory(categoryDTO, db.getNextFreeCategoryId());
    }

    public static String updateCategory(CarCategoryDTO categoryDTO, String id) {
        dbInitCheck();
        db.putCategory(id, categoryDTO);
        return id;
    }
}
