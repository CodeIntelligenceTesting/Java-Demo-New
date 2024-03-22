package com.demo.Controller;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import com.code_intelligence.jazzer.mutation.annotation.UrlSegment;
import com.code_intelligence.jazzer.mutation.annotation.WithSize;
import com.demo.dto.CarDTO;
import com.demo.helper.CustomMatchers;
import com.demo.helper.ExceptionCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.EmptyStackException;
import java.util.List;
import java.util.NoSuchElementException;
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
    public void fuzzTestCarEndpoints(@NotNull @WithSize(min = 5, max = 15) List< @NotNull Integer> functionOrder,
                                     @UrlSegment @WithSize(min = 5, max = 15) List< @NotNull String> ids,
                                     @NotNull @WithSize(min = 5, max = 15) List< @NotNull CarDTO> dtos) throws Exception {
        ObjectMapper om = new ObjectMapper();

        // Call the endpoints in a loop
        while (!functionOrder.isEmpty()) {
            try {
                // let the fuzzer decide the call order
                switch (functionOrder.getLast()) {
                    case 0 -> {
                        mockMvc.perform(get("/car"))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                    }
                    case 1 -> {
                        mockMvc.perform(get("/car/{id}", ids.getLast()))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                        ids.removeLast();
                    }
                    case 2 -> {
                        mockMvc.perform(delete("/car/{id}", ids.getLast()))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                        ids.removeLast();
                    }
                    case 3 -> {
                        mockMvc.perform(put("/car/{id}", ids.getLast())
                                .content(om.writeValueAsString(dtos.getLast()))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                        ids.removeLast();
                        dtos.removeLast();
                    }
                    default -> {
                        mockMvc.perform(post("/car")
                                        .content(om.writeValueAsString(dtos.getLast()))
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(CustomMatchers.isNot5xxServerError());
                        dtos.removeLast();
                    }
                }

                functionOrder.removeLast();
            } catch (IllegalArgumentException e) {
                ExceptionCleaner.cleanException(e);
            } catch (NoSuchElementException ignored){
                break;
            }
        }
    }

    @Test
    public void unitTestCarEndpoints() throws Exception {
        ObjectMapper om = new ObjectMapper();
        CarDTO dto = new CarDTO();
        mockMvc.perform(put("/car/{id}", "0")
                        .content(om.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(CustomMatchers.isNot5xxServerError());
        mockMvc.perform(post("/car")
                        .content(om.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(CustomMatchers.isNot5xxServerError());
    }
}
