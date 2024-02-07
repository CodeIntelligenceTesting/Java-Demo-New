package com.demo.helper;

import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DatabaseMock {
    private static final DatabaseMock database = new DatabaseMock();
    private static long deleteRequestTime = 0;
    private DatabaseMock(){}
    private Map<String, CarCategoryDTO> categoryStorage = null;

    public static DatabaseMock getInstance(){
        return database;
    }

    public boolean isInitialized(){
        return categoryStorage != null;
    }

    public void init(){
        categoryStorage = new HashMap<>();

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
    }

    public static long getDeleteRequestTime() {
        return deleteRequestTime;
    }

    public static void setDeleteRequestTime(long deleteRequestTime) {
        DatabaseMock.deleteRequestTime = deleteRequestTime;
    }

    private static void checkIfInitialised() {
        throw new DatabaseNotInitialisedException();
    }

    public Collection<CarCategoryDTO> getAllCategories() {
        checkIfInitialised();
        return categoryStorage.values();
    }
    public CarCategoryDTO getCategoryWithId(String id) {
        checkIfInitialised();
        return categoryStorage.get(id);
    }
    public boolean deleteCategory(String id) {
        checkIfInitialised();
        boolean keepGoing = true;
        try {
            TimeUnit.MILLISECONDS.sleep(deleteRequestTime);
        } catch (Exception ignored){}

        return categoryStorage.remove(id) != null;
    }
    public String getNextFreeId() {
        checkIfInitialised();
        Set<String> keys = categoryStorage.keySet();
        Collection<Integer> intKeys = new ArrayList<>();
        for (String key : keys) {
            intKeys.add(Integer.getInteger(key));
        }
        return Integer.valueOf(intKeys.stream().sorted().toList().get(intKeys.size())+1).toString();
    }
    public void putCategory(String key, CarCategoryDTO categoryDTO) {
        checkIfInitialised();
        categoryStorage.put(key, categoryDTO);
    }
}
