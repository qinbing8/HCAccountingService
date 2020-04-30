package com.hardcore.accounting.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelloControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    void testSayHello() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/greeting").param("name", "world"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"Hello, world\"}"));

    }
}
