package com.commercex.auth;

import com.commercex.auth.dto.request.LoginRequest;
import com.commercex.auth.repository.UserSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        // Arrange
        testDataFactory.createVerifiedCustomer();

        LoginRequest request = new LoginRequest(
                "kanchan@test.com",
                "Password@123"
        );

        // Act + Assert
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        // Verify session created
        assertTrue(userSessionRepository.count() > 0);
    }

    @Test
    void shouldFailForWrongPassword() throws Exception {

        testDataFactory.createVerifiedCustomer();

        LoginRequest request = new LoginRequest(
                "kanchan@test.com",
                "WrongPassword"
        );

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailForDisabledUser() throws Exception {

        testDataFactory.createDisabledCustomer();

        LoginRequest request = new LoginRequest(
                "disabled@test.com",
                "Password@123"
        );

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized());
    }
}