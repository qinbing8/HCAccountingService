package com.hardcore.accounting.converter;

import com.hardcore.accounting.converter.c2s.UserInfoC2SConverter;
import com.hardcore.accounting.model.common.UserInfo;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserInfoC2SConverterTest {

    private UserInfoC2SConverter userInfoC2SConverter = new UserInfoC2SConverter();

    @Test
    void testDoForward() {
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

        // Act
        val result = userInfoC2SConverter.convert(userInfoInCommon);

        // Assert
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", userId)
                .hasFieldOrPropertyWithValue("username", username)
                .hasFieldOrPropertyWithValue("password", password);
    }

    @Test
    void testDoBackward() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";

        val userInfoInCommon = com.hardcore.accounting.model.service.UserInfo.builder()
                .id(userId)
                .username(username)
                .password(password)
                .build();

        // Act
        val result = userInfoC2SConverter.reverse().convert(userInfoInCommon);

        // Assert
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", userId)
                .hasFieldOrPropertyWithValue("username", username)
                .hasFieldOrPropertyWithValue("password", password);
    }
}
