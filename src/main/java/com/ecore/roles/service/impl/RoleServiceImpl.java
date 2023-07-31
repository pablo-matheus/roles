package com.ecore.roles.service.impl;

import com.ecore.roles.dto.RoleDto;
import com.ecore.roles.entity.Role;
import com.ecore.roles.mapper.RoleMapper;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final RoleMapper roleMapper;

    @Override
    public RoleDto findById(String roleId) {
        return roleRepository.findById(roleId)
                .map(roleMapper::toDto)
                .orElse(null);
    }

    @Override
    public boolean existsById(String id) {
        return roleRepository.existsById(id);
    }

    @Override
    public RoleDto findByName(String name) {
        Role role = roleRepository.findByName(name);
        return roleMapper.toDto(role);
    }

    @Override
    public boolean exists(RoleDto roleDto) {
        return roleDto != null;
    }

    @Transactional
    private RoleDto save(RoleDto roleDto) {
        Role role = roleMapper.toEntity(roleDto);

        if (roleRepository.existsByName(role.getName())) {
            log.error("Error while trying to create Role, the Role with name [{}] already exists", roleDto.getName());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A Role with the same name already exists");
        }

        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        return this.save(roleDto);
    }

    @Override
    public RoleDto findRoleByTeamIdAndUserId(String teamId, String userId) {
        Role role = roleRepository.findByRoleMembership_TeamIdAndRoleMembership_UserId(teamId, userId);

        if (role == null) {
            log.warn("There is no Role assigned to the User [{}] in the the Team [{}]",
                    userId, teamId);

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Role was found with Team ID and User ID");
        }

        return roleMapper.toDto(role);
    }

}
