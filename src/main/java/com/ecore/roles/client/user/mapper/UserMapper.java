package com.ecore.roles.client.user.mapper;

import com.ecore.roles.client.user.dto.UserDto;
import com.ecore.roles.client.user.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserResponse userResponse);

}
