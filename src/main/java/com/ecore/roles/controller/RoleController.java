package com.ecore.roles.controller;

import com.ecore.roles.dto.RoleDto;
import com.ecore.roles.dto.RoleMembershipDto;
import com.ecore.roles.mapper.AssignRoleMapper;
import com.ecore.roles.mapper.RoleMapper;
import com.ecore.roles.mapper.RoleMembershipMapper;
import com.ecore.roles.request.AssignRoleRequest;
import com.ecore.roles.request.RoleRequest;
import com.ecore.roles.response.RoleMembershipResponse;
import com.ecore.roles.response.RoleResponse;
import com.ecore.roles.service.RoleMembershipService;
import com.ecore.roles.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class RoleController {

    @Autowired
    private final RoleService roleService;

    @Autowired
    private final RoleMembershipService roleMembershipService;

    @Autowired
    private final RoleMapper roleMapper;

    @Autowired
    private final RoleMembershipMapper roleMembershipMapper;

    @Autowired
    private final AssignRoleMapper assignRoleMapper;

    @Operation(summary = "Create a new Role")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/roles")
    public RoleResponse createRole(@Valid @RequestBody RoleRequest roleRequest) {
        RoleDto roleDto = roleService.createRole(roleMapper.toDto(roleRequest));
        return roleMapper.toResponse(roleDto);
    }

    @Operation(summary = "Look up a Role for a Membership")
    @GetMapping("/teams/{team-id}/users/{user-id}/roles")
    public RoleResponse getRoleByTeamIdAndUserId(@PathVariable("team-id") String teamId,
                                                 @PathVariable("user-id") String userId) {

        RoleDto roleDto = roleService.findRoleByTeamIdAndUserId(teamId, userId);
        return roleMapper.toResponse(roleDto);
    }

    @Operation(summary = "Look up Memberships for a Role")
    @GetMapping("/roles/{role-id}/teams/users")
    public List<RoleMembershipResponse> getMembersByRole(@PathVariable("role-id") String roleId) {
        List<RoleMembershipDto> roleMembershipDtoList = roleMembershipService.findMembersByRole(roleId);
        return roleMembershipMapper.toResponseList(roleMembershipDtoList);
    }

    @Operation(summary = "Assign a Role to a Team member")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/teams/{team-id}/users/{user-id}/roles")
    public RoleMembershipResponse assignRole(@PathVariable("team-id") String teamId,
                                             @PathVariable("user-id") String userId,
                                             @RequestBody AssignRoleRequest assignRoleRequest) {

        RoleMembershipDto roleMembershipDto = roleMembershipService.assignRole(teamId, userId, assignRoleMapper.toDto(assignRoleRequest));
        return roleMembershipMapper.toResponse(roleMembershipDto);
    }

}
