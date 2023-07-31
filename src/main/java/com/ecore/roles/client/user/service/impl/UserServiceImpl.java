package com.ecore.roles.client.user.service.impl;

import com.ecore.roles.client.user.UserClient;
import com.ecore.roles.client.user.dto.UserDto;
import com.ecore.roles.client.user.mapper.UserMapper;
import com.ecore.roles.client.user.response.UserResponse;
import com.ecore.roles.client.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserClient userClient;

    @Autowired
    private final UserMapper userMapper;

    @Override
    public UserDto findById(String id) {
        UserResponse userResponse = userClient.getById(id);
        return userMapper.toDto(userResponse);
    }

    @Override
    public boolean existsById(String id) {
        return findById(id) != null;
    }

}
