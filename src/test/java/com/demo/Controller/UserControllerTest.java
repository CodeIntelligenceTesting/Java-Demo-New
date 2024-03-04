package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.demo.dto.CarCategoryDTO;
import com.demo.dto.UserDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.DatabaseMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * {@link UserController#getUser(String, String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestGetUser(String id, String role) throws Exception {
        mockMvc.perform(get("/user/"+id)
                        .param("role", role))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link UserController#getUsers(String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestGetUsers(String role) throws Exception {
        mockMvc.perform(get("/user")
                        .param("role", role))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link UserController#deleteUser(String, String)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestDeleteUser(String id, String role, long requestTime) throws Exception {
        DatabaseMock.setDeleteRequestTime(requestTime);
        DatabaseMock.getInstance().init();
        mockMvc.perform(delete("/user/"+id)
                        .param("role", role))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link UserController#updateOrCreateUser(String, String, UserDTO)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestUpdateOrCreateUser(String id, String role, UserDTO userDTO) throws Exception {
        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(put("/user/"+id)
                        .param("role", role)
                        .content(om.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }

    /**
     * {@link UserController#createUser(String, UserDTO)}
     * @param role
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestCreateUser(String role, UserDTO userDTO) throws Exception {
        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(put("/user")
                        .param("role", role)
                        .content(om.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }
}
