package com.ecore.roles.client.team.service.impl;

import com.ecore.roles.client.team.TeamClient;
import com.ecore.roles.client.team.service.TeamService;
import com.ecore.roles.client.team.dto.TeamDto;
import com.ecore.roles.client.team.mapper.TeamMapper;
import com.ecore.roles.client.team.response.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private final TeamClient teamClient;

    @Autowired
    private final TeamMapper teamMapper;

    @Override
    public TeamDto findById(String id) {
        TeamResponse teamResponse = teamClient.getById(id);
        return teamMapper.toDto(teamResponse);
    }

    @Override
    public boolean existsById(String id) {
        return this.findById(id) != null;
    }

    @Override
    public boolean exists(TeamDto teamDto) {
        return teamDto != null;
    }

    @Override
    public boolean isTeamMember(TeamDto teamDto, String userId) {
        List<String> teamMembers = Optional.ofNullable(teamDto)
                .map(TeamDto::getTeamMemberIds)
                .orElse(new ArrayList<>());

        return teamMembers.contains(userId);
    }

}
