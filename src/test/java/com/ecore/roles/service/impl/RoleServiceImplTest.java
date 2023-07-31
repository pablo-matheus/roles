package com.ecore.roles.service.impl;

import com.ecore.roles.dto.RoleDto;
import com.ecore.roles.entity.Role;
import com.ecore.roles.mapper.RoleMapper;
import com.ecore.roles.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;


    @Test
    void givenValidRoleWhenCreateRoleThenReturnCreatedRole() {
        RoleDto roleDto = new RoleDto();
        Role roleEntity = new Role();
        RoleDto expectedRoleDto = new RoleDto();

        BDDMockito.given(roleMapper.toEntity(roleDto)).willReturn(roleEntity);
        BDDMockito.given(roleRepository.existsByName(roleEntity.getName())).willReturn(false);
        BDDMockito.given(roleRepository.save(roleEntity)).willReturn(roleEntity);
        BDDMockito.given(roleMapper.toDto(roleEntity)).willReturn(expectedRoleDto);

        RoleDto result = roleService.createRole(roleDto);

        Assertions.assertEquals(expectedRoleDto, result);

        BDDMockito.verify(roleMapper, BDDMockito.times(1)).toEntity(roleDto);
        BDDMockito.verify(roleRepository, BDDMockito.times(1)).existsByName(roleEntity.getName());
        BDDMockito.verify(roleRepository, BDDMockito.times(1)).save(roleEntity);
        BDDMockito.verify(roleMapper, BDDMockito.times(1)).toDto(roleEntity);
        BDDMockito.verifyNoMoreInteractions(roleMapper);
        BDDMockito.verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void givenExistingRoleWhenCreateRoleThenThrowException() {
        RoleDto roleDto = new RoleDto();
        Role roleEntity = new Role();

        BDDMockito.given(roleMapper.toEntity(roleDto)).willReturn(roleEntity);
        BDDMockito.given(roleRepository.existsByName(roleEntity.getName())).willReturn(true);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> roleService.createRole(roleDto));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("A Role with the same name already exists", exception.getReason());

        BDDMockito.verify(roleMapper, BDDMockito.times(1)).toEntity(roleDto);
        BDDMockito.verify(roleRepository, BDDMockito.times(1)).existsByName(roleEntity.getName());
        BDDMockito.verifyNoMoreInteractions(roleRepository);
        BDDMockito.verifyNoMoreInteractions(roleMapper);
    }
    
    @Test
    void givenExistingRoleWhenFindByIdThenReturnRole() {
        String roleId = "123";
        Role roleEntity = new Role();
        RoleDto expectedRoleDto = new RoleDto();

        BDDMockito.given(roleRepository.findById(roleId)).willReturn(Optional.of(roleEntity));
        BDDMockito.given(roleMapper.toDto(roleEntity)).willReturn(expectedRoleDto);

        RoleDto result = roleService.findById(roleId);

        Assertions.assertEquals(expectedRoleDto, result);

        BDDMockito.verify(roleRepository, BDDMockito.times(1)).findById(roleId);
        BDDMockito.verify(roleMapper, BDDMockito.times(1)).toDto(roleEntity);
        BDDMockito.verifyNoMoreInteractions(roleRepository);
        BDDMockito.verifyNoMoreInteractions(roleMapper);
    }

    @Test
    void givenNonExistingRoleWhenFindByIdThenReturnNull() {
        String roleId = "123";

        BDDMockito.given(roleRepository.findById(roleId)).willReturn(Optional.empty());

        RoleDto result = roleService.findById(roleId);

        Assertions.assertNull(result);

        BDDMockito.verify(roleRepository, BDDMockito.times(1)).findById(roleId);
        BDDMockito.verifyNoMoreInteractions(roleRepository);
        BDDMockito.verifyNoInteractions(roleMapper);
    }

    @Test
    void givenExistingRoleWhenFindRoleByMembershipThenReturnRole() {
        String teamId = "123";
        String userId = "123";
        Role roleEntity = new Role();
        RoleDto expectedRoleDto = new RoleDto();

        BDDMockito.given(roleRepository.findByRoleMembership_TeamIdAndRoleMembership_UserId(teamId, userId))
                .willReturn(roleEntity);

        BDDMockito.given(roleMapper.toDto(roleEntity)).willReturn(expectedRoleDto);

        RoleDto result = roleService.findRoleByTeamIdAndUserId(teamId, userId);

        Assertions.assertEquals(expectedRoleDto, result);

        BDDMockito.verify(roleRepository, BDDMockito.times(1))
                .findByRoleMembership_TeamIdAndRoleMembership_UserId(teamId, userId);

        BDDMockito.verify(roleMapper, BDDMockito.times(1)).toDto(roleEntity);
        BDDMockito.verifyNoMoreInteractions(roleRepository);
        BDDMockito.verifyNoMoreInteractions(roleMapper);
    }

    @Test
    void givenNonExistingRoleWhenFindRoleByMembershipThenThrowException() {
        String teamId = "123";
        String userId = "123";

        BDDMockito.given(roleRepository.findByRoleMembership_TeamIdAndRoleMembership_UserId(teamId, userId))
                .willReturn(null);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> roleService.findRoleByTeamIdAndUserId(teamId, userId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("No Role was found with Team ID and User ID", exception.getReason());

        BDDMockito.verify(roleRepository, BDDMockito.times(1))
                .findByRoleMembership_TeamIdAndRoleMembership_UserId(teamId, userId);

        BDDMockito.verifyNoMoreInteractions(roleRepository);
        BDDMockito.verifyNoInteractions(roleMapper);
    }

}

