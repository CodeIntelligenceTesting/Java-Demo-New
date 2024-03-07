package com.demo.helper;

import com.demo.Controller.CarCategoryController;
import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.handler.CarCategoryHandler;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Database Mock class used to simulate DB requests, etc.
 */
public class DatabaseMock {
    private static final DatabaseMock database = new DatabaseMock();
    private static long deleteRequestTime = 0;
    private DatabaseMock(){}
    private Map<String, CarCategoryDTO> categoryStorage = null;
    private Map<String, UserDTO> userStorage = null;

    public static DatabaseMock getInstance(){
        return database;
    }
    public boolean isInitialized(){
        return categoryStorage == null || userStorage == null;
    }
    public void init(){
        categoryStorage = new HashMap<>();
        userStorage = new HashMap<>();

        categoryStorage.put("1", new CarCategoryDTO(
                "Small and budget friendly cars",
                CarCategoryDTO.TrunkSize.ONE_CASE,
                4,
                "Open Corsa",
                UserDTO.Role.DEFAULT_USER));
        categoryStorage.put("2", new CarCategoryDTO(
                "Exclusive Convertibles",
                CarCategoryDTO.TrunkSize.ONE_CASE,
                4,
                "Porsche Turbo Convertible",
                UserDTO.Role.VIP_USER));

        userStorage.put("1",
                new UserDTO(
                        UserDTO.Role.DEFAULT_USER,
                        "default-user",
                        "default-user@email.com",
                        "SuperSecurePassword",
                        1));
        userStorage.put("2",
                new UserDTO(
                        UserDTO.Role.VIP_USER
                        , "vip-user",
                        "vip-user@email.com",
                        "SuperSecurePassword",
                        2));
        userStorage.put("3",
                new UserDTO(
                        UserDTO.Role.ADMIN
                        , "admin",
                        "admin@email.com",
                        "SuperSecurePassword!!!",
                        3));
    }

    public static long getDeleteRequestTime() {
        return deleteRequestTime;
    }
    public static void setDeleteRequestTime(long deleteRequestTime) {
        DatabaseMock.deleteRequestTime = Math.abs(deleteRequestTime);
    }
    private static void checkIfInitialised() {
        if (database.isInitialized()) {
            throw new DatabaseNotInitialisedException();
        }
    }
    public Collection<CarCategoryDTO> getAllCategories() {
        checkIfInitialised();
        return categoryStorage.values();
    }
    public CarCategoryDTO getCategoryWithId(String id) {
        checkIfInitialised();
        return categoryStorage.get(id);
    }

    /**
     * Delete function that can take longer to execute then expected.
     * Called ultimately by {@link CarCategoryController#deleteCategory(String, String)}
     * Directly called by {@link CarCategoryHandler#deleteCategory(String)}
     * @param id category id
     * @return if object was deleted successful. False if category was not stored.
     */
    public boolean deleteCategory(String id) {
        checkIfInitialised();
        try {
            TimeUnit.MILLISECONDS.sleep(deleteRequestTime);
        } catch (Exception ignored){}

        return categoryStorage.remove(id) != null;
    }
    public String getNextFreeCategoryId() {
        checkIfInitialised();
        return Integer.valueOf(getLargestNumberFromKeySet(categoryStorage.keySet())).toString();
    }
    public void putCategory(String key, CarCategoryDTO categoryDTO) {
        checkIfInitialised();
        categoryStorage.put(key, categoryDTO);
    }
    public Collection<UserDTO> getAllUsers() {
        checkIfInitialised();
        return userStorage.values();
    }
    public UserDTO getUserWithId(String id) {
        checkIfInitialised();
        return userStorage.get(id);
    }
    public boolean deleteUser(String id) {
        checkIfInitialised();
        return userStorage.remove(id) != null;
    }
    public String getNextFreeUserId() {
        checkIfInitialised();
        return Integer.valueOf(getLargestNumberFromKeySet(userStorage.keySet())).toString();
    }
    public void putUser(String key, UserDTO userDTO) {
        checkIfInitialised();
        userStorage.put(key, userDTO);
    }
    private int getLargestNumberFromKeySet(Set<String> keys) {
        Collection<Integer> intKeys = new ArrayList<>();
        for (String key : keys) {
            intKeys.add(Integer.getInteger(key));
        }
        List<Integer> list = new ArrayList<>();
        for (Integer intKey : intKeys) {
            list.add(intKey);
        }
        list.sort(null);
        return list.get(intKeys.size()) + 1;
    }
}
