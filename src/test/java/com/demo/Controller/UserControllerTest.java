package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.UrlSegment;
import com.code_intelligence.jazzer.mutation.annotation.WithUtf8Length;
import com.demo.dto.UserDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.DatabaseMock;
import com.demo.helper.ExceptionCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
     * Fuzz test function that checks the {@link UserController#getUsers(String)} endpoint.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.UserControllerTest::fuzzTestGetUsers</code> or
     * <code>cifuzz container run com.demo.Controller.UserControllerTest::fuzzTestGetUsers</code>.
     * Code contains no issues and testing will stop after the timeout specified in the cifuzz.yaml (Default 30m)
     * <p/>
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
     */
    @FuzzTest
    public void fuzzTestGetUsers(@NotNull String role) throws Exception {
        try {
            mockMvc.perform(get("/user")
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }
    /**
     * Unit test variant of {@link UserControllerTest#fuzzTestGetUsers(String)}
     * @throws Exception uncaught exceptions to signal failing test
     */
    @Test
    public void unitTestGetUsers() throws Exception {
        try {
            mockMvc.perform(get("/user")
                            .param("role", "DEFAULT"))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    /**
     * Fuzz test function that checks the {@link UserController#getUser(String, String)} endpoint.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.UserControllerTest::fuzzTestGetUser</code> or
     * <code>cifuzz container run com.demo.Controller.UserControllerTest::fuzzTestGetUser</code>.
     * Finds a security issue in form of a Remote-Code-Execution (RCE) vulnerability.
     * <p/>
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
     */
    @FuzzTest
    public void fuzzTestGetUser(@NotNull @WithUtf8Length(min=1, max=5) String id, @NotNull String role) throws Exception {
        try {
            mockMvc.perform(get("/user/{id}", id)
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }

    }

    /**
     * Fuzz test function that checks the {@link UserController#deleteUser(String, String)} endpoint.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.UserControllerTest::fuzzTestDeleteUser</code> or
     * <code>cifuzz container run com.demo.Controller.UserControllerTest::fuzzTestDeleteUser</code>.
     * Code contains no issues and testing will stop after the timeout specified in the cifuzz.yaml (Default 30m)
     * <p/>
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
     */
    @FuzzTest
    public void fuzzTestDeleteUser(@NotNull @WithUtf8Length(min=1, max=5) String id,
                                   @NotNull String role) throws Exception {

        try {
            DatabaseMock.getInstance().init();
            mockMvc.perform(delete("/user/{id}", id)
                            .param("role", role))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }

    }

    /**
     * Fuzz test function that checks the {@link UserController#updateOrCreateUser(String, String, UserDTO)} endpoint.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.UserControllerTest::fuzzTestUpdateOrCreateUser</code> or
     * <code>cifuzz container run com.demo.Controller.UserControllerTest::fuzzTestUpdateOrCreateUser</code>.
     * Finds a security issue in form of an SQL Injection vulnerability.
     * <p/>
     * @param id parameter filled in by the fuzzer.
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
     */
    @FuzzTest
    public void fuzzTestUpdateOrCreateUser(@NotNull @WithUtf8Length(min=1, max=5) String id,
                                           @NotNull String role,
                                           @NotNull UserDTO userDTO) throws Exception {
        try {
        ObjectMapper om = new ObjectMapper();
            mockMvc.perform(put("/user/{id}", id)
                            .param("role", role)
                            .content(om.writeValueAsString(userDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }

    /**
     * Fuzz test function that checks the {@link UserController#createUser(String, UserDTO)}endpoint.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.UserControllerTest::fuzzTestCreateUser</code> or
     * <code>cifuzz container run com.demo.Controller.UserControllerTest::fuzzTestCreateUser</code>.
     * Finds a security issue in form of an LDAP Injection vulnerability.
     * <p/>
     * @param role parameter filled in by the fuzzer.
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
     */
    @FuzzTest
    public void fuzzTestCreateUser(@NotNull String role, @NotNull UserDTO userDTO) throws Exception {
        try {
            ObjectMapper om = new ObjectMapper();
            mockMvc.perform(post("/user")
                            .param("role", role)
                            .content(om.writeValueAsString(userDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(CustomMatchers.isNot5xxServerError());
        } catch (IllegalArgumentException e) {
            ExceptionCleaner.cleanException(e);
        }
    }
}
