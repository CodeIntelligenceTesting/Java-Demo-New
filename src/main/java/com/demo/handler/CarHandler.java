package com.demo.handler;

import com.demo.dto.CarDTO;
import com.demo.helper.CarIdGenerationException;
import com.demo.helper.DatabaseMock;

import java.util.Collection;

/**
 * Handler class that provides methods for controller class to utilize.
 */
public class CarHandler {

    private static final DatabaseMock db = DatabaseMock.getInstance();
    /**
     * Helper function to make sure DB connection is initialized
     */
    private static void dbInitCheck() {
        if (db.isInitialized()) {
            db.init();
        }
    }

    /**
     * Secure handler function that returns all saved cars
     * @return collection of car dtos
     */
    public static Collection<CarDTO> getCars() {
        dbInitCheck();
        return db.getAllCars();
    }

    /**
     * Secure handler function that returns the specific car or null
     * @param id id of the specific car
     * @return car dto or null
     */
    public static CarDTO getCarWithId(String id) {
        dbInitCheck();
        return db.getCarWithId(id);
    }

    /**
     * Secure handler function that deletes the specific car if it exists
     * @param id id of the specific car
     * @return boolean if deletion was successful
     */
    public static boolean deleteCarWithId(String id){
        dbInitCheck();
        return db.deleteCarWithId(id);
    }

    /**
     * Create new car handler function with robustness issue.
     * Throws exception if created
     * @param dto car dto to save
     */
    public static void createNewCar(CarDTO dto){
        dbInitCheck();
        String nextId = db.getNextCarId();
        if (db.getCarWithId(nextId) != null) {
            throw new CarIdGenerationException("Call to get next id resulted in already existing id.");
        } else {
            db.createOrUpdateCar(dto, nextId);
        }
    }

    /**
     * Secure handler update handler function that updates car infos
     * @param dto new data to save
     * @param id id of car
     */
    public static void updateCar(CarDTO dto, String id){
        dbInitCheck();
        db.createOrUpdateCar(dto, id);
    }

}