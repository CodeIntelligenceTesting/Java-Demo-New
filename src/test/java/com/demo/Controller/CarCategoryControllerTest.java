package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.DatabaseMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
    public void fuzzTestGetCategory(String id, String role) throws Exception {
        mockMvc.perform(get("/category/"+id)
                        .param("role", role))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link CarCategoryController#getCategories(String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestGetCategories(String role) throws Exception {
        mockMvc.perform(get("/category")
                        .param("role", role))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link CarCategoryController#deleteCategory(String, String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestDeleteCategories(String id, String role, long requestTime) throws Exception {
        DatabaseMock.setDeleteRequestTime(requestTime);
        DatabaseMock.getInstance().init();
        mockMvc.perform(delete("/category/"+id)
                        .param("role", role))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link CarCategoryController#updateOrCreateCategory(String, String, CarCategoryDTO)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestUpdateOrCreateCategory(String id, String role, CarCategoryDTO categoryDTO) throws Exception {
        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(put("/category/"+id)
                        .param("role", role)
                        .content(om.writeValueAsString(categoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link CarCategoryController#createCategory(String, CarCategoryDTO)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestCreateCategory(String role, CarCategoryDTO categoryDTO) throws Exception {
        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(post("/category")
                        .param("role", role)
                        .content(om.writeValueAsString(categoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }
}
