package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import com.demo.dto.UserDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.DatabaseMock;
import com.demo.helper.ExceptionCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Fuzz test function that checks the {@link UserController#getUser(String, String)} endpoint.
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
     */
    @FuzzTest
    public void fuzzTestGetUser(@NotNull String id, @NotNull String role) throws Exception {
        try {
            mockMvc.perform(get("/user/{id}", id)
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }

    }

    /**
     * Fuzz test function that checks the {@link UserController#getUsers(String)} endpoint.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
     */
    @FuzzTest
    public void fuzzTestGetUsers(@NotNull String role) throws Exception {
        // TODO deleted for you to fill in
    }

    /**
     * Fuzz test function that checks the {@link UserController#deleteUser(String, String)} endpoint.
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
     */
    @FuzzTest
    public void fuzzTestDeleteUser(@NotNull @WithUtf8Length(min=1, max=5) String id,
                                   @NotNull String role) throws Exception {

        // TODO deleted for you to fill in

    }

    /**
     * Fuzz test function that checks the {@link UserController#updateOrCreateUser(String, String, UserDTO)} endpoint.
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
     */
    @FuzzTest
    public void fuzzTestUpdateOrCreateUser(@NotNull @WithUtf8Length(min=1, max=5) String id,
                                           @NotNull String role,
                                           @NotNull UserDTO userDTO) throws Exception {
        // TODO deleted for you to fill in
    }

    /**
     * Fuzz test function that checks the {@link UserController#createUser(String, UserDTO)}endpoint.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception default exception
     */
    @FuzzTest
    public void fuzzTestCreateUser(@NotNull String role, @NotNull UserDTO userDTO) throws Exception {
        // TODO deleted for you to fill in
    }
}
