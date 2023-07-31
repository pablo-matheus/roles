package com.ecore.roles.service.impl;

import com.ecore.roles.client.team.dto.TeamDto;
import com.ecore.roles.client.team.service.TeamService;
import com.ecore.roles.dto.AssignRoleDto;
import com.ecore.roles.dto.RoleDto;
import com.ecore.roles.dto.RoleMembershipDto;
import com.ecore.roles.entity.RoleMembership;
import com.ecore.roles.mapper.RoleMembershipMapper;
import com.ecore.roles.repository.RoleMembershipRepository;
import com.ecore.roles.service.RoleMembershipService;
import com.ecore.roles.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleMembershipServiceImpl implements RoleMembershipService {

    @Autowired
    private final RoleService roleService;

    @Autowired
    private final TeamService teamService;

    @Autowired
    private final RoleMembershipRepository roleMembershipRepository;

    @Autowired
    private final RoleMembershipMapper roleMembershipMapper;

    @Transactional
    @Override
    public RoleMembershipDto save(RoleMembershipDto roleMembershipDto) {
        RoleDto roleDto = roleService.findByName(roleMembershipDto.getRoleDto().getName());

        if (!roleService.exists(roleDto)) {
            log.warn("It was not possible to assign the Role, there is no Role with ID [{}]",
                    roleMembershipDto.getRoleDto().getId());

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role does not exist");
        }

        roleMembershipDto.setRoleDto(roleDto);

        TeamDto teamDto = teamService.findById(roleMembershipDto.getTeamId());

        if (!teamService.exists(teamDto)) {
            log.warn("It was not possible to assign the Role, there is no Team with ID [{}]",
                    roleMembershipDto.getTeamId());

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team does not exist");
        }

        if (!teamService.isTeamMember(teamDto, roleMembershipDto.getUserId())) {
            log.warn("It was not possible to assign the Role, there is no User with ID [{}] in the Team [{}]",
                    roleMembershipDto.getUserId(), roleMembershipDto.getTeamId());

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not part of the Team");
        }

        RoleMembership roleMembership = roleMembershipMapper.toEntity(roleMembershipDto);

        if (roleMembershipRepository.existsByTeamIdAndUserId(roleMembership.getTeamId(), roleMembership.getUserId())) {
            log.warn("It was not possible to assign the Role, the User with ID [{}] in the Team [{}] already has a Role",
                    roleMembership.getTeamId(), roleMembership.getUserId());

            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has a registered Role in this Team");
        }

        RoleMembership savedRoleMembership;
        try {
            savedRoleMembership = roleMembershipRepository.save(roleMembership);
        } catch (Exception exception) {
            log.error("Error while trying to assign the role, there was a problem during the data persistence using the Role ID [{}], Team ID [{}] and User ID [{}]",
                    roleMembershipDto.getRoleDto().getId(), roleMembershipDto.getTeamId(), roleMembershipDto.getUserId(), exception);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error persisting data", exception);
        }

        return roleMembershipMapper.toDto(savedRoleMembership);
    }

    @Override
    public RoleMembershipDto assignRole(String teamId, String userId, AssignRoleDto assignRoleDto) {
        RoleMembershipDto roleMembershipDto = RoleMembershipDto.builder()
                .roleDto(new RoleDto(null, assignRoleDto.getName(), null))
                .teamId(teamId)
                .userId(userId)
                .build();

        return this.save(roleMembershipDto);
    }

    @Override
    public List<RoleMembershipDto> findMembersByRole(String roleId) {
        List<RoleMembership> roleMembershipList = roleMembershipRepository.findByRoleId(roleId);
        return roleMembershipMapper.toDtoList(roleMembershipList);
    }

}
