package com.ecore.roles.client.team.mapper;

import com.ecore.roles.client.team.response.TeamResponse;
import com.ecore.roles.client.team.dto.TeamDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDto toDto(TeamResponse teamResponse);

}
