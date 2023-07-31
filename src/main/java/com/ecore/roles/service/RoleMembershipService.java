package com.ecore.roles.service;

import com.ecore.roles.dto.AssignRoleDto;
import com.ecore.roles.dto.RoleMembershipDto;

import java.util.List;

public interface RoleMembershipService {

    RoleMembershipDto assignRole(String teamId, String userId, AssignRoleDto assignRoleDto);

    List<RoleMembershipDto> findMembersByRole(String roleId);

}
