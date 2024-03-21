package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.demo.dto.CarDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.ExceptionCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.EmptyStackException;
import java.util.Stack;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Fuzz test function that checks the {@link CarController} endpoints.
     * <p/>
     * Execute test with <code>cifuzz run com.demo.Controller.CarControllerTest::fuzzTestCarEndpoints</code> or
     * <code>cifuzz container run com.demo.Controller.CarControllerTest::fuzzTestCarEndpoints</code>.
     * Finds a state dependent issue in form of an uncaught CarIdGenerationException exception.
     * <p/>
     * @throws Exception uncaught exceptions for the fuzzer to detect issues.
     */
    @FuzzTest
    public void fuzzTestCarEndpoints(@NotNull Stack<Integer> functionOrder, @NotNull Stack<String> ids, @NotNull Stack<CarDTO> dtos) throws Exception {
        // Check for a minimal size that makes sense to execute the tests
        if (functionOrder.size() < 5 || ids.size() < 5 || dtos.size() < 5) {
            return;
        }
        ObjectMapper om = new ObjectMapper();

        // Call the endpoints in a loop
        while (!functionOrder.isEmpty()) {
            try {
                // let the fuzzer decide the call order
                switch (functionOrder.pop()) {
                    case 0 -> {
                        mockMvc.perform(get("/car"))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                    }
                    case 1 -> {
                        mockMvc.perform(get("/car/{id}", ids.pop()))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                    }
                    case 2 -> {
                        mockMvc.perform(delete("/car/{id}", ids.pop()))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                    }
                    case 3 -> {
                        mockMvc.perform(put("/car/{id}", ids.pop())
                                .content(om.writeValueAsString(dtos.pop()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                    }
                    default -> {
                        mockMvc.perform(post("/car")
                                        .content(om.writeValueAsString(dtos.pop()))
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                    }
                }
            } catch (IllegalArgumentException e) {
                ExceptionCleaner.cleanException(e);
            } catch (EmptyStackException ignored){
                break;
            }
        }
    }
}
