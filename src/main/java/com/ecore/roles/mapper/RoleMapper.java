package com.ecore.roles.mapper;

import com.ecore.roles.dto.RoleDto;
import com.ecore.roles.entity.Role;
import com.ecore.roles.request.RoleRequest;
import com.ecore.roles.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper extends DefaultMapper {

    RoleResponse toResponse(RoleDto roleDto);

    RoleDto toDto(RoleRequest roleRequest);

    RoleDto toDto(Role role);

    @Mapping(target = "name", source = "dto.name", qualifiedByName = "toUpperSnakeCase")
    Role toEntity(RoleDto dto);

}
