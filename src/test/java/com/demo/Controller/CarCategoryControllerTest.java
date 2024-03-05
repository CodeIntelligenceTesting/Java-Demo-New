package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import com.demo.dto.CarCategoryDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.DatabaseMock;
import com.demo.helper.ExceptionCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class CarCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Fuzz test function that checks the {@link CarCategoryController#getCategories(String)} endpoint.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
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
     * Fuzz test function that checks the {@link CarCategoryController#getCategory(String, String)} endpoint.
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
     */
    @FuzzTest
    public void fuzzTestGetCategory(@NotNull String id, @NotNull String role) throws Exception {
        // TODO deleted for you to fill in
    }

    /**
     * Advanced Fuzz test function that checks the {@link CarCategoryController#deleteCategory(String, String)} endpoint.
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
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

    /**
     * Fuzz test function that checks the {@link CarCategoryController#updateOrCreateCategory(String, String, CarCategoryDTO)} endpoint.
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @param categoryDTO parameter filled in by the fuzzer.
     * @throws Exception default exception
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
     * Fuzz test function that checks the {@link CarCategoryController#createCategory(String, CarCategoryDTO)} endpoint.
     * @param role parameter filled in by the fuzzer.
     * @param categoryDTO parameter filled in by the fuzzer.
     * @throws Exception default exception
     */
    @FuzzTest
    public void fuzzTestCreateCategory(@NotNull String role, @NotNull CarCategoryDTO categoryDTO) throws Exception {
        // TODO deleted for you to fill in
    }
}
