package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.DatabaseMock;
import com.demo.helper.ExceptionCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class CarCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    /**
     * {@link CarCategoryController#getCategory(String, String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestGetCategory(@NotNull String id, @NotNull String role) throws Exception {
        try {
            mockMvc.perform(get("/category/{id}", id)
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    /**
     * {@link CarCategoryController#getCategories(String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestGetCategories(@NotNull String role) throws Exception {
        try {
            mockMvc.perform(get("/category")
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    /**
     * {@link CarCategoryController#deleteCategory(String, String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    @Timeout(5)
    public void fuzzTestDeleteCategory(@NotNull @WithUtf8Length(min=1, max=5) String id,
                                       @NotNull String role,
                                       long requestTime) throws Exception {
        try {
            DatabaseMock.getInstance().init();
            DatabaseMock.setDeleteRequestTime(requestTime);

            mockMvc.perform(delete("/category/{id}", id)
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    @Test
    @Timeout(5)
    public void unitTestDeleteCategory() throws Exception {
        @NotNull String id = "1234";
        @NotNull String role = "QURNSU4=";
        long requestTime = 1_000_000;
        try {
            DatabaseMock.getInstance().init();
            DatabaseMock.setDeleteRequestTime(requestTime);

            System.out.println("Before calling Endpoint");
            mockMvc.perform(delete("/category/{id}", id)
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    /**
     * {@link CarCategoryController#updateOrCreateCategory(String, String, CarCategoryDTO)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestUpdateOrCreateCategory(@NotNull @WithUtf8Length(min=1, max=5) String id,
                                               @NotNull String role,
                                               @NotNull CarCategoryDTO categoryDTO) throws Exception {
        try {
            ObjectMapper om = new ObjectMapper();
            mockMvc.perform(put("/category/{id}", id)
                            .param("role", role)
                            .content(om.writeValueAsString(categoryDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    /**
     * {@link CarCategoryController#createCategory(String, CarCategoryDTO)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestCreateCategory(@NotNull String role, @NotNull CarCategoryDTO categoryDTO) throws Exception {
        try {
            ObjectMapper om = new ObjectMapper();
            mockMvc.perform(post("/category")
                            .param("role", role)
                            .content(om.writeValueAsString(categoryDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }
}
