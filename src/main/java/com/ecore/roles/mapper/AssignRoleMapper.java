package com.ecore.roles.mapper;

import com.ecore.roles.dto.AssignRoleDto;
import com.ecore.roles.request.AssignRoleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignRoleMapper extends DefaultMapper {

    @Mapping(target = "name", source = "assignRoleRequest.name", qualifiedByName = "toUpperSnakeCase")
    AssignRoleDto toDto(AssignRoleRequest assignRoleRequest);

}
