package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.junit.Lifecycle;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import com.demo.dto.CarCategoryDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.DatabaseMock;
import com.demo.helper.ExceptionCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.CarCategoryControllerTest::fuzzTestGetCategories</code> or
     * <code>cifuzz container run com.demo.Controller.CarCategoryControllerTest::fuzzTestGetCategories</code>.
     * Finds a robustness issue in form of an uncaught NullPointerException exception.
     * <p/>
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
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
     * Unit test variant of {@link CarCategoryControllerTest#fuzzTestGetCategories(String)}
     * @throws Exception uncaught exceptions to signal failing test
     */
    @Test
    public void unitTestGetCategories() throws Exception {
        try {
            mockMvc.perform(get("/category")
                            .param("role", "DEFAULT"))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    /**
     * Fuzz test function that checks the {@link CarCategoryController#getCategory(String, String)} endpoint.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.CarCategoryControllerTest::fuzzTestGetCategory</code> or
     * <code>cifuzz container run com.demo.Controller.CarCategoryControllerTest::fuzzTestGetCategory</code>.
     * Finds a robustness issue in form of an uncaught NullPointerException exception.
     * <p/>
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
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
     * Advanced Fuzz test function that checks the {@link CarCategoryController#deleteCategory(String, String)} endpoint.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.CarCategoryControllerTest::fuzzTestDeleteCategory</code> or
     * <code>cifuzz container run com.demo.Controller.CarCategoryControllerTest::fuzzTestDeleteCategory</code>.
     * Finds a robustness/timeout issue.
     * <p/>
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
     */
    @Timeout(5)
    @FuzzTest
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
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.CarCategoryControllerTest::fuzzTestUpdateOrCreateCategory</code> or
     * <code>cifuzz container run com.demo.Controller.CarCategoryControllerTest::fuzzTestUpdateOrCreateCategory</code>.
     * Code contains no issues and testing will stop after the timeout specified in the cifuzz.yaml (Default 30m)
     * <p/>
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @param categoryDTO parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
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
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.CarCategoryControllerTest::fuzzTestCreateCategory</code> or
     * <code>cifuzz container run com.demo.Controller.CarCategoryControllerTest::fuzzTestCreateCategory</code>.
     * Finds a robustness issue in form of an uncaught DatabaseNotInitialisedException exception.
     * <p/>
     * @param role parameter filled in by the fuzzer.
     * @param categoryDTO parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
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
