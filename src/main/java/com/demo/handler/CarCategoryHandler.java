package com.demo.handler;

import com.demo.Controller.CarCategoryController;
import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.helper.DatabaseMock;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Handler class that provides methods for controller class to utilize.
 */
public class CarCategoryHandler {

    private static final DatabaseMock db = DatabaseMock.getInstance();

    /**
     * Helper function to make sure DB connection is initialized
     */
    private static void dbInitCheck(){
        if (db.isInitialized()) {
            db.init();
        }
    }

    /**
     * Handler function with robustness issue, that handles querying of all vip categories
     * Called by {@link CarCategoryController#getCategories(String)}.
     * @return collection of category objects
     */
    public static Collection<CarCategoryDTO> returnVIPCategories(){
        // missing DB init step
        Collection<CarCategoryDTO> vipCategories = new ArrayList<>();
        for (CarCategoryDTO category : db.getAllCategories()) {
            if (category.getVisibleTo() == UserDTO.Role.VIP_USER) {
                vipCategories.add(category);
            }
        }

        return vipCategories;
    }

    /**
     * Robust handler function that handles querying of all standard categories
     * Called by {@link CarCategoryController#getCategories(String)}.
     * @return collection of category objects
     */
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

    /**
     * Handler function that returns the requested category or null, if it does not exist.
     * Called by {@link CarCategoryController#getCategory(String, String)}
     * @param id category id
     * @return category dto or null
     */
    public static CarCategoryDTO returnSpecificCategory(String id) {
        dbInitCheck();
        return db.getCategoryWithId(id);
    }

    /**
     * Handler function that attempts to delete specified category.
     * Advanced robustness issue is hidden in the simulated network delay.
     * Called by {@link CarCategoryController#deleteCategory(String, String)}
     * Request time is hidden here: {@link DatabaseMock#deleteCategory(String)}
     * @param id category id to delete.
     * @return if deletion was successful. False if category did not exist.
     */
    public static boolean deleteCategory(String id) {
        dbInitCheck();
        return db.deleteCategory(id);
    }

    /**
     * Vulnerable handler function that requests an ID from the DB without checking if it's initialized.
     * Called by {@link CarCategoryController#createCategory(String, CarCategoryDTO)}
     * @param categoryDTO dto to save
     * @return id of newly saved category.
     */
    public static String createCategory(CarCategoryDTO categoryDTO){
        // db.getNextFreeCategoryId() is a call to the db without checking if initialized
        return updateCategory(categoryDTO, db.getNextFreeCategoryId());
    }

    /**
     * Robust handler function that updates or creates a new user with a given id.
     * Called by {@link CarCategoryController#updateOrCreateCategory(String, String, CarCategoryDTO)}
     * @param categoryDTO dto object with new category infos
     * @param id id of category
     * @return the id of the changed object
     */
    public static String updateCategory(CarCategoryDTO categoryDTO, String id) {
        dbInitCheck();
        db.putCategory(id, categoryDTO);
        return id;
    }
}
