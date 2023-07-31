package com.ecore.roles.client.user.service.impl;

import com.ecore.roles.client.user.UserClient;
import com.ecore.roles.client.user.dto.UserDto;
import com.ecore.roles.client.user.mapper.UserMapper;
import com.ecore.roles.client.user.response.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserClient userClient;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void givenExistingUserWhenFindByIdThenReturnUser() {
        String userId = "123";

        UserResponse userResponse = new UserResponse();
        UserDto expectedUserDto = new UserDto();

        BDDMockito.given(userClient.getById(userId)).willReturn(userResponse);
        BDDMockito.given(userMapper.toDto(userResponse)).willReturn(expectedUserDto);

        UserDto result = userService.findById(userId);

        Assertions.assertEquals(expectedUserDto, result);

        BDDMockito.verify(userClient, BDDMockito.times(1)).getById(userId);
        BDDMockito.verify(userMapper, BDDMockito.times(1)).toDto(userResponse);

        BDDMockito.verifyNoMoreInteractions(userClient);
        BDDMockito.verifyNoMoreInteractions(userMapper);
    }

    @Test
    void givenNonExistingUserWhenFindByIdThenReturnNull() {
        String userId = "123";

        BDDMockito.given(userClient.getById(userId)).willReturn(null);
        BDDMockito.given(userMapper.toDto(null)).willReturn(null);

        UserDto result = userService.findById(userId);

        Assertions.assertNull(result);

        BDDMockito.verify(userClient, BDDMockito.times(1)).getById(userId);
        BDDMockito.verify(userMapper, BDDMockito.times(1)).toDto(null);

        BDDMockito.verifyNoMoreInteractions(userClient);
        BDDMockito.verifyNoMoreInteractions(userMapper);
    }

    @Test
    void givenExistingUserWhenExistsByIdThenReturnTrue() {
        String userId = "123";

        UserResponse userResponse = new UserResponse();
        UserDto expectedUserDto = new UserDto();

        BDDMockito.given(userClient.getById(userId)).willReturn(userResponse);
        BDDMockito.given(userMapper.toDto(userResponse)).willReturn(expectedUserDto);

        boolean result = userService.existsById(userId);

        Assertions.assertTrue(result);

        BDDMockito.verify(userClient, BDDMockito.times(1)).getById(userId);
        BDDMockito.verify(userMapper, BDDMockito.times(1)).toDto(userResponse);

        BDDMockito.verifyNoMoreInteractions(userClient);
        BDDMockito.verifyNoMoreInteractions(userMapper);
    }

    @Test
    void givenNonExistingUserWhenExistsByIdThenReturnFalse() {
        String userId = "123";

        BDDMockito.given(userClient.getById(userId)).willReturn(null);
        BDDMockito.given(userMapper.toDto(null)).willReturn(null);

        boolean result = userService.existsById(userId);

        Assertions.assertFalse(result);

        BDDMockito.verify(userClient, BDDMockito.times(1)).getById(userId);
        BDDMockito.verify(userMapper, BDDMockito.times(1)).toDto(null);

        BDDMockito.verifyNoMoreInteractions(userClient);
        BDDMockito.verifyNoMoreInteractions(userMapper);
    }

}
