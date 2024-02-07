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
public class UserController {
    private DatabaseMock database = DatabaseMock.getInstance();

    @GetMapping("/user/{id}")
    public CarCategoryDTO getUser(@PathVariable String id, @RequestParam(defaultValue = "DEFAULT_USER") String role) {
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
        // Not clean but easiest way to return a 403.
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/user")
    public Collection<CarCategoryDTO> getUsers(@RequestParam String role) {
        Collection<CarCategoryDTO> categories = new ArrayList<>();
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.VIP_USER) {
            categories.addAll(CarCategoryHandler.returnVIPCategories());
        }
        categories.addAll(CarCategoryHandler.returnDefaultCategories());

        return categories;
    }

    @DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable String id, @RequestParam String role) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            return CarCategoryHandler.deleteCategory(id);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/user/{id}")
    public String updateOrCreateUser(@PathVariable String id, @RequestParam String role, @RequestBody CarCategoryDTO dto) {
        if (UserDTO.Role.fromString(role) == UserDTO.Role.ADMIN) {
            return CarCategoryHandler.updateCategory(dto, id);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/user")
    public String createUser(@RequestParam String role, @RequestBody CarCategoryDTO dto) {
        if (UserDTO.Role.fromString(role) == UserDTO.Role.ADMIN) {
            return CarCategoryHandler.createCategory(dto);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
