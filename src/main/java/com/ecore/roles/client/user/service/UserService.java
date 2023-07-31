package com.ecore.roles.client.user.service;

import com.ecore.roles.client.user.dto.UserDto;

public interface UserService {

    UserDto findById(String id);

    boolean existsById(String id);

}
