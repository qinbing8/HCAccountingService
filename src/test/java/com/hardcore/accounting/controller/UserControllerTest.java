package com.hardcore.accounting.controller;

import com.hardcore.accounting.converter.c2s.UserInfoC2SConverter;
import com.hardcore.accounting.exception.GlobalExceptionHandler;
import com.hardcore.accounting.exception.ResourceNotFoundException;
import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.common.UserInfo;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserInfoC2SConverter userInfoC2SConverter;

    @Mock
    private UserInfoManager userInfoManager;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    public void teardown() {
        reset(userInfoManager);
        reset(userInfoC2SConverter);
    }

    @Test
    public void testGetUserInfoByUserId() throws Exception {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";
        val createTime = LocalDate.now();

        val userInfoInCommon = UserInfo.builder()
                .id(userId)
                .username(username)
                .password(password)
                .build();

        val userInfo = com.hardcore.accounting.model.service.UserInfo
                .builder()
                .id(userId)
                .username(username)
                .password(password)
                .build();

        doReturn(userInfoInCommon).when(userInfoManager).getUserInfoByUserId(anyLong());

        doReturn(userInfo).when(userInfoC2SConverter).convert(userInfoInCommon);


        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("{\"id\":100,\"username\":\"hardcore\",\"password\":\"hardcore\"}"));

        verify(userInfoManager).getUserInfoByUserId(anyLong());
        verify(userInfoC2SConverter).convert(userInfoInCommon);
    }

    @Test
    public void testGetUserInfoByUserIdWithInvalidUserId() throws Exception {
        // Arrange
        val userId = -100L;
        doThrow(new ResourceNotFoundException(String.format("User %s was not found", userId)))
                .when(userInfoManager)
                .getUserInfoByUserId(anyLong());

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/v1.0/users/" + userId))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("{\"code\":\"INVALID_PARAMETER\",\"errorType\":\"Client\",\"message\":\"The user id -100 is invalid\",\"statusCode\":400}"));
    }
}
