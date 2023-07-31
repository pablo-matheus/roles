package com.ecore.roles.mapper;

import com.ecore.roles.dto.RoleMembershipDto;
import com.ecore.roles.entity.RoleMembership;
import com.ecore.roles.response.RoleMembershipResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMembershipMapper extends DefaultMapper {

    @Mapping(target = "roleResponse", source = "roleMembershipDto.roleDto")
    RoleMembershipResponse toResponse(RoleMembershipDto roleMembershipDto);

    List<RoleMembershipResponse> toResponseList(List<RoleMembershipDto> roleMembershipDtoList);

    @Mapping(target = "role", source = "roleMembershipDto.roleDto")
    RoleMembership toEntity(RoleMembershipDto roleMembershipDto);

    @Mapping(target = "roleDto", source = "roleMembership.role")
    RoleMembershipDto toDto(RoleMembership roleMembership);

    List<RoleMembershipDto> toDtoList(List<RoleMembership> roleMembershipList);

}
