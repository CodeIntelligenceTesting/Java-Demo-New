package com.demo.Controller;

import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.handler.CarCategoryHandler;
import com.demo.helper.DatabaseMock;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@RestController()
public class CarCategoryController {

    /**
     * GET endpoint with a robustness issue, that returns all categories.
     * It's never checked that the DB is initialized before querying for all categories that are only visible for VIP_USERS
     * Issue is hidden in {@link CarCategoryHandler#returnVIPCategories()}.
     * @param role querying user role
     * @return a collection of the DTO objects. Will be sent to the browser as JSON list.
     */
    @GetMapping("/category")
    public Collection<CarCategoryDTO> getCategories(@RequestParam (required = false) String role) {
        Collection<CarCategoryDTO> categories = new ArrayList<>();
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.VIP_USER) {
            // got here if the role value was "VklQX1VTRVI="
            //CarCategoryHandler.returnVIPCategories() never checks if DB is initialized before trying to access it.
            categories.addAll(CarCategoryHandler.returnVIPCategories());
        }
        categories.addAll(CarCategoryHandler.returnDefaultCategories());

        return categories;
    }

    /**
     * GET endpoint with a robustness issue, that returns one specific category.
     * There is no validation that a category exists before trying to access it.
     * @param id category id
     * @param role requesting user role definition
     * @return CarCategoryDTO that is mapped to a JSON object when being returned
     */
    @GetMapping("/category/{id}")
    public CarCategoryDTO getCategory(@PathVariable String id, @RequestParam(defaultValue = "DEFAULT_USER") String role) {
        // No existence check is made after querying for the category
        CarCategoryDTO category = CarCategoryHandler.returnSpecificCategory(id);
        if (category.getVisibleTo() == UserDTO.Role.DEFAULT_USER) {
            return category;
        } else if (category.getVisibleTo() == UserDTO.Role.VIP_USER) {
            if (UserDTO.Role.fromString(role) == UserDTO.Role.VIP_USER) {
                return category;
            }
        } else if (category.getVisibleTo() == UserDTO.Role.ADMIN) {
            if (UserDTO.Role.fromString(role) == UserDTO.Role.ADMIN) {
                return category;
            }
        }
        // Not clean but easiest way to return a 404.
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    /**
     * DELETE endpoint with advanced robustness issue. Simulates Network issues when sending delete. Can cause Timeouts.
     * Issue is hidden in {@link DatabaseMock#deleteCategory(String)}.
     * @param id category id
     * @param role requesting user role definition
     * @return if deletion request was successful
     */
    @DeleteMapping("/category/{id}")
    public boolean deleteCategory(@PathVariable String id, @RequestParam (required = false) String role) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            // got here if the role value was "QURNSU4="
            // Request can be pending for a while and cause timeouts
            return CarCategoryHandler.deleteCategory(id);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Robust PUT endpoint. No issues in this one.
     * @param id category id
     * @param role requesting user role definition
     * @param dto mapped JSON DTO that contains the new information to be stored
     * @return returns String ID of the changed/inserted object
     */
    @PutMapping("/category/{id}")
    public String updateOrCreateCategory(@PathVariable String id, @RequestParam (required = false) String role, @RequestBody CarCategoryDTO dto) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            // got here if the role value was "QURNSU4="
            return CarCategoryHandler.updateCategory(dto, id);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * POST endpoint with a robustness issue. Tries to create new objects and letting the server chose the new ID.
     * Tries to access DB directly before checking if it's initialised.
     * Issue is hidden in {@link CarCategoryHandler#createCategory(CarCategoryDTO)}.
     * @param role requesting user role definition
     * @param dto mapped JSON DTO that contains the new information to be stored
     * @return returns String ID of the changed/inserted object
     */
    @PostMapping("/category")
    public String createCategory(@RequestParam (required = false) String role, @RequestBody CarCategoryDTO dto) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            // got here if the role value was "QURNSU4="
            // Can cause DBNotInitialised Exception by accessing db before checking if it's initialised.
            return CarCategoryHandler.createCategory(dto);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
