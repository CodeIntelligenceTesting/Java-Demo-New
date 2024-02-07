package com.demo.security;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.demo.security.helper.JsonAddBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class ComplexSecurityExampleTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * {@link ComplexSecurityExample#addNewUser(JsonAddBody)}
     * @param jsonAddBody
     * @throws Exception
     */
    @FuzzTest
    public void fuzzTestAddNewUser(JsonAddBody jsonAddBody) throws Exception {
        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(post("/add")
                .content(om.writeValueAsString(jsonAddBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
