package com.demo.Controller;

import com.demo.dto.CarCategoryDTO;
import com.demo.dto.CarDTO;
import com.demo.dto.UserDTO;
import com.demo.handler.CarCategoryHandler;
import com.demo.handler.CarHandler;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

public class CarController {
    // create example where an update call with an id is able to create new instances,
    // but the create call does not check if there are ids already taken and tries to override them.

    /**
     * Secure GET endpoint that returns all cars.
     * @return a collection of the DTO objects. Will be sent to the browser as JSON list.
     */
    @GetMapping("/car")
    public Collection<CarDTO> getCars() {
        return CarHandler.getCars();
    }

    /**
     * Secure GET endpoint that returns the car with the specified id.
     * @param id car id
     * @return the DTO object. Will be sent to the browser as JSON object.
     */
    @GetMapping("/car/{id}")
    public CarDTO getCar(@PathVariable String id) {
        return CarHandler.getCarWithId(id);
    }

    /**
     * Secure DELETE endpoint that deletes the car with the specified id.
     * @param id car id
     * @return JSON boolean
     */
    @DeleteMapping("/car/{id}")
    public boolean deleteCar(@PathVariable String id) {
        return CarHandler.deleteCarWithId(id);
    }

    /**
     * Insecure POST endpoint that crashes when there was a PUT call before this POST call,
     * that created a new Car object with the same id that this POST endpoint got assigned as new position from the DB.
     * @param dto new DTO to create
     */
    @PostMapping("/car")
    public void createCar(@RequestBody CarDTO dto) {
        // Missing try-catch block
        CarHandler.createNewCar(dto);
    }

    /**
     * Secure PUT endpoint that creates or updates the car with the specified id.
     * @param id car id
     */
    @PutMapping("/car/{id}")
    public void updateCar(@PathVariable String id, @RequestBody CarDTO dto) {
        CarHandler.updateCar(dto, id);
    }
}
