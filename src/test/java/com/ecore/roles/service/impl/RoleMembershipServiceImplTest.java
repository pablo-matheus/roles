package com.ecore.roles.service.impl;

import com.ecore.roles.client.team.dto.TeamDto;
import com.ecore.roles.client.team.service.TeamService;
import com.ecore.roles.dto.AssignRoleDto;
import com.ecore.roles.dto.RoleDto;
import com.ecore.roles.dto.RoleMembershipDto;
import com.ecore.roles.entity.RoleMembership;
import com.ecore.roles.mapper.RoleMembershipMapper;
import com.ecore.roles.repository.RoleMembershipRepository;
import com.ecore.roles.service.RoleService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RoleMembershipServiceImplTest {

    @Mock
    private RoleService roleService;

    @Mock
    private TeamService teamService;

    @Mock
    private RoleMembershipRepository roleMembershipRepository;

    @Mock
    private RoleMembershipMapper roleMembershipMapper;

    @InjectMocks
    private RoleMembershipServiceImpl roleMembershipService;

    @Test
    void givenValidParametersWhenAssignRoleThenReturnAssignmentInformation() {
        String roleId = "123";
        String teamId = "123";
        String userId = "123";
        String roleName = "test";

        TeamDto teamDto = new TeamDto();
        teamDto.setId(teamId);

        RoleDto roleDto = new RoleDto(roleId, roleName, null);

        RoleMembershipDto roleMembershipDto = new RoleMembershipDto();
        roleMembershipDto.setRoleDto(roleDto);

        RoleMembership roleMembership = new RoleMembership();
        roleMembership.setTeamId(teamId);
        roleMembership.setUserId(userId);

        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setName(roleName);

        BDDMockito.given(roleService.findByName(roleName)).willReturn(roleDto);
        BDDMockito.given(roleService.exists(roleDto)).willReturn(true);

        BDDMockito.given(teamService.findById(teamId)).willReturn(teamDto);
        BDDMockito.given(teamService.exists(teamDto)).willReturn(true);
        BDDMockito.given(teamService.isTeamMember(teamDto, userId)).willReturn(true);

        BDDMockito.given(roleMembershipMapper.toEntity(BDDMockito.any(RoleMembershipDto.class))).willReturn(roleMembership);

        BDDMockito.given(roleMembershipRepository.existsByTeamIdAndUserId(teamId, userId)).willReturn(false);
        BDDMockito.given(roleMembershipRepository.save(BDDMockito.any(RoleMembership.class))).willReturn(roleMembership);

        BDDMockito.given(roleMembershipMapper.toDto(BDDMockito.any(RoleMembership.class))).willReturn(roleMembershipDto);

        RoleMembershipDto result = roleMembershipService.assignRole(roleId, teamId, assignRoleDto);

        Assertions.assertEquals(roleMembershipDto, result);

        BDDMockito.verify(roleService, BDDMockito.times(1)).findByName(roleName);
        BDDMockito.verify(roleService, BDDMockito.times(1)).exists(roleDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).findById(teamId);
        BDDMockito.verify(teamService, BDDMockito.times(1)).exists(teamDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).isTeamMember(teamDto, userId);
        BDDMockito.verify(roleMembershipMapper, BDDMockito.times(1)).toEntity(BDDMockito.any(RoleMembershipDto.class));
        BDDMockito.verify(roleMembershipRepository, BDDMockito.times(1)).existsByTeamIdAndUserId(teamId, userId);
        BDDMockito.verify(roleMembershipRepository, BDDMockito.times(1)).save(BDDMockito.any(RoleMembership.class));
        BDDMockito.verify(roleMembershipMapper, BDDMockito.times(1)).toDto(roleMembership);

        BDDMockito.verifyNoMoreInteractions(roleService);
        BDDMockito.verifyNoMoreInteractions(teamService);
        BDDMockito.verifyNoMoreInteractions(roleMembershipMapper);
        BDDMockito.verifyNoMoreInteractions(roleMembershipRepository);
    }

    @Test
    void givenNonExistingRoleWhenAssignRoleThenThrowException() {
        String roleId = "123";
        String teamId = "123";
        String roleName = "test";

        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setName(roleName);

        RoleDto roleDto = new RoleDto(null, null, null);

        BDDMockito.given(roleService.findByName(roleName)).willReturn(roleDto);
        BDDMockito.given(roleService.exists(roleDto)).willReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> roleMembershipService.assignRole(roleId, teamId, assignRoleDto));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Role does not exist", exception.getReason());

        BDDMockito.verify(roleService, BDDMockito.times(1)).findByName(roleName);
        BDDMockito.verify(roleService, BDDMockito.times(1)).exists(roleDto);

        BDDMockito.verifyNoInteractions(teamService);
        BDDMockito.verifyNoInteractions(roleMembershipMapper);
        BDDMockito.verifyNoInteractions(roleMembershipRepository);

        BDDMockito.verifyNoMoreInteractions(roleService);
    }

    @Test
    void givenNonExistingTeamWhenAssignRoleThenThrowException() {
        String roleId = "123";
        String teamId = "123";
        String roleName = "test";

        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setName(roleName);

        RoleDto roleDto = new RoleDto(roleId, roleName, null);

        BDDMockito.given(roleService.findByName(roleName)).willReturn(roleDto);
        BDDMockito.given(roleService.exists(roleDto)).willReturn(true);

        BDDMockito.given(teamService.findById(teamId)).willReturn(null);
        BDDMockito.given(teamService.exists(null)).willReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> roleMembershipService.assignRole(roleId, teamId, assignRoleDto));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Team does not exist", exception.getReason());

        BDDMockito.verify(roleService, BDDMockito.times(1)).findByName(roleName);
        BDDMockito.verify(roleService, BDDMockito.times(1)).exists(roleDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).findById(teamId);
        BDDMockito.verify(teamService, BDDMockito.times(1)).exists(null);

        BDDMockito.verifyNoInteractions(roleMembershipMapper);
        BDDMockito.verifyNoInteractions(roleMembershipRepository);

        BDDMockito.verifyNoMoreInteractions(roleService);
        BDDMockito.verifyNoMoreInteractions(teamService);
    }

    @Test
    void givenNonTeamMemberWhenAssignRoleThenThrowException() {
        String roleId = "123";
        String teamId = "123";
        String userId = "123";
        String roleName = "test";

        TeamDto teamDto = new TeamDto();

        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setName(roleName);

        RoleDto roleDto = new RoleDto(roleId, roleName, null);

        BDDMockito.given(roleService.findByName(roleName)).willReturn(roleDto);
        BDDMockito.given(roleService.exists(roleDto)).willReturn(true);

        BDDMockito.given(teamService.findById(teamId)).willReturn(teamDto);
        BDDMockito.given(teamService.exists(teamDto)).willReturn(true);
        BDDMockito.given(teamService.isTeamMember(teamDto, userId)).willReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> roleMembershipService.assignRole(roleId, teamId, assignRoleDto));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("User is not part of the Team", exception.getReason());

        BDDMockito.verify(roleService, BDDMockito.times(1)).findByName(roleName);
        BDDMockito.verify(roleService, BDDMockito.times(1)).exists(roleDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).findById(teamId);
        BDDMockito.verify(teamService, BDDMockito.times(1)).exists(teamDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).isTeamMember(teamDto, userId);

        BDDMockito.verifyNoInteractions(roleMembershipMapper);
        BDDMockito.verifyNoInteractions(roleMembershipRepository);

        BDDMockito.verifyNoMoreInteractions(roleService);
        BDDMockito.verifyNoMoreInteractions(teamService);
    }

    @Test
    void givenUserAlreadyHasRoleInTeamWhenAssignRoleThenThrowException() {
        String roleId = "123";
        String teamId = "123";
        String userId = "123";
        String roleName = "test";

        TeamDto teamDto = new TeamDto();

        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setName(roleName);

        RoleMembership roleMembership = new RoleMembership();
        roleMembership.setTeamId(teamId);
        roleMembership.setUserId(userId);

        RoleDto roleDto = new RoleDto(roleId, roleName, null);

        BDDMockito.given(roleService.findByName(roleName)).willReturn(roleDto);
        BDDMockito.given(roleService.exists(roleDto)).willReturn(true);

        BDDMockito.given(teamService.findById(teamId)).willReturn(teamDto);
        BDDMockito.given(teamService.exists(teamDto)).willReturn(true);
        BDDMockito.given(teamService.isTeamMember(teamDto, userId)).willReturn(true);

        BDDMockito.given(roleMembershipMapper.toEntity(BDDMockito.any(RoleMembershipDto.class))).willReturn(roleMembership);

        BDDMockito.given(roleMembershipRepository.existsByTeamIdAndUserId(teamId, userId)).willReturn(true);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> roleMembershipService.assignRole(roleId, teamId, assignRoleDto));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("User already has a registered Role in this Team", exception.getReason());

        BDDMockito.verify(roleService, BDDMockito.times(1)).findByName(roleName);
        BDDMockito.verify(roleService, BDDMockito.times(1)).exists(roleDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).findById(teamId);
        BDDMockito.verify(teamService, BDDMockito.times(1)).exists(teamDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).isTeamMember(teamDto, userId);
        BDDMockito.verify(roleMembershipRepository, BDDMockito.times(1)).existsByTeamIdAndUserId(teamId, userId);

        BDDMockito.verifyNoMoreInteractions(roleService);
        BDDMockito.verifyNoMoreInteractions(teamService);
        BDDMockito.verifyNoMoreInteractions(roleMembershipMapper);
        BDDMockito.verifyNoMoreInteractions(roleMembershipRepository);
    }

    @Test
    void givenDatabaseProblemWhenAssignRoleThenThrowException() {
        String roleId = "123";
        String teamId = "123";
        String userId = "123";
        String roleName = "test";

        TeamDto teamDto = new TeamDto();

        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setName(roleName);

        RoleMembership roleMembership = new RoleMembership();
        roleMembership.setTeamId(teamId);
        roleMembership.setUserId(userId);

        RoleDto roleDto = new RoleDto(roleId, roleName, null);

        BDDMockito.given(roleService.findByName(roleName)).willReturn(roleDto);
        BDDMockito.given(roleService.exists(roleDto)).willReturn(true);

        BDDMockito.given(teamService.findById(teamId)).willReturn(teamDto);
        BDDMockito.given(teamService.exists(teamDto)).willReturn(true);
        BDDMockito.given(teamService.isTeamMember(teamDto, userId)).willReturn(true);

        BDDMockito.given(roleMembershipMapper.toEntity(BDDMockito.any(RoleMembershipDto.class))).willReturn(roleMembership);

        BDDMockito.given(roleMembershipRepository.existsByTeamIdAndUserId(teamId, userId)).willReturn(false);

        BDDMockito.given(roleMembershipRepository.save(BDDMockito.any(RoleMembership.class)))
                .willThrow(ConstraintViolationException.class);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> roleMembershipService.assignRole(roleId, teamId, assignRoleDto));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        Assertions.assertEquals("Error persisting data", exception.getReason());

        BDDMockito.verify(roleService, BDDMockito.times(1)).findByName(roleName);
        BDDMockito.verify(roleService, BDDMockito.times(1)).exists(roleDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).findById(teamId);
        BDDMockito.verify(teamService, BDDMockito.times(1)).exists(teamDto);
        BDDMockito.verify(teamService, BDDMockito.times(1)).isTeamMember(teamDto, userId);
        BDDMockito.verify(roleMembershipRepository, BDDMockito.times(1)).existsByTeamIdAndUserId(teamId, userId);
        BDDMockito.verify(roleMembershipRepository, BDDMockito.times(1)).save(BDDMockito.any(RoleMembership.class));

        BDDMockito.verifyNoMoreInteractions(roleService);
        BDDMockito.verifyNoMoreInteractions(teamService);
        BDDMockito.verifyNoMoreInteractions(roleMembershipMapper);
        BDDMockito.verifyNoMoreInteractions(roleMembershipRepository);
    }

    @Test
    void givenValidRoleIdWhenFindMembersByRoleThenReturnMembers() {
        String roleId = "123";

        RoleMembership roleMembership = new RoleMembership();
        RoleMembershipDto roleMembershipDto = new RoleMembershipDto();
        List<RoleMembership> roleMembershipList = Collections.singletonList(roleMembership);

        BDDMockito.given(roleMembershipRepository.findByRoleId(roleId)).willReturn(roleMembershipList);

        BDDMockito.given(roleMembershipMapper.toDtoList(roleMembershipList)).willReturn(Collections.singletonList(roleMembershipDto));

        List<RoleMembershipDto> result = roleMembershipService.findMembersByRole(roleId);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(roleMembershipDto, result.get(0));

        BDDMockito.verify(roleMembershipRepository, BDDMockito.times(1)).findByRoleId(roleId);
        BDDMockito.verify(roleMembershipMapper, BDDMockito.times(1)).toDtoList(roleMembershipList);

        BDDMockito.verifyNoMoreInteractions(roleMembershipRepository);
        BDDMockito.verifyNoMoreInteractions(roleMembershipMapper);
    }

}
