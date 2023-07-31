package com.ecore.roles.service;

import com.ecore.roles.dto.RoleDto;

public interface RoleService {

    RoleDto findById(String id);

    boolean existsById(String id);

    RoleDto findByName(String name);

    boolean exists(RoleDto roleDto);

    RoleDto save(RoleDto roleDto);

    RoleDto createRole(RoleDto roleDto);

    RoleDto findRoleByTeamIdAndUserId(String teamId, String userId);

}
