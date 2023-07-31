package com.ecore.roles.client.team.service;

import com.ecore.roles.client.team.dto.TeamDto;

public interface TeamService {

    TeamDto findById(String id);

    boolean existsById(String id);

    boolean exists(TeamDto teamDto);

    boolean isTeamMember(TeamDto teamDto, String userId);

}
