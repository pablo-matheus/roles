package com.ecore.roles.client.team.service.impl;

import com.ecore.roles.client.team.TeamClient;
import com.ecore.roles.client.team.dto.TeamDto;
import com.ecore.roles.client.team.mapper.TeamMapper;
import com.ecore.roles.client.team.response.TeamResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamClient teamClient;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void givenExistingTeamWhenFindByIdThenReturnTeam() {
        String teamId = "123";

        TeamResponse teamResponse = new TeamResponse();
        TeamDto expectedTeamDto = new TeamDto();

        BDDMockito.given(teamClient.getById(teamId)).willReturn(teamResponse);
        BDDMockito.given(teamMapper.toDto(teamResponse)).willReturn(expectedTeamDto);

        TeamDto result = teamService.findById(teamId);

        Assertions.assertEquals(expectedTeamDto, result);

        BDDMockito.verify(teamClient, BDDMockito.times(1)).getById(teamId);
        BDDMockito.verify(teamMapper, BDDMockito.times(1)).toDto(teamResponse);

        BDDMockito.verifyNoMoreInteractions(teamClient);
        BDDMockito.verifyNoMoreInteractions(teamMapper);
    }

    @Test
    void givenNonExistingTeamWhenFindByIdThenReturnNull() {
        String teamId = "123";

        BDDMockito.given(teamClient.getById(teamId)).willReturn(null);
        BDDMockito.given(teamMapper.toDto(null)).willReturn(null);

        TeamDto result = teamService.findById(teamId);

        Assertions.assertNull(result);

        BDDMockito.verify(teamClient, BDDMockito.times(1)).getById(teamId);
        BDDMockito.verify(teamMapper, BDDMockito.times(1)).toDto(null);

        BDDMockito.verifyNoMoreInteractions(teamClient);
        BDDMockito.verifyNoMoreInteractions(teamMapper);
    }

    @Test
    void givenExistingTeamWhenExistsByIdThenReturnTrue() {
        String teamId = "123";

        TeamResponse teamResponse = new TeamResponse();
        TeamDto expectedTeamDto = new TeamDto();

        BDDMockito.given(teamClient.getById(teamId)).willReturn(teamResponse);
        BDDMockito.given(teamMapper.toDto(teamResponse)).willReturn(expectedTeamDto);

        boolean result = teamService.existsById(teamId);

        Assertions.assertTrue(result);

        BDDMockito.verify(teamClient, BDDMockito.times(1)).getById(teamId);
        BDDMockito.verify(teamMapper, BDDMockito.times(1)).toDto(teamResponse);

        BDDMockito.verifyNoMoreInteractions(teamClient);
        BDDMockito.verifyNoMoreInteractions(teamMapper);
    }

    @Test
    void givenNonExistingTeamWhenExistsByIdThenReturnFalse() {
        String teamId = "123";

        BDDMockito.given(teamClient.getById(teamId)).willReturn(null);
        BDDMockito.given(teamMapper.toDto(null)).willReturn(null);

        boolean result = teamService.existsById(teamId);

        Assertions.assertFalse(result);

        BDDMockito.verify(teamClient, BDDMockito.times(1)).getById(teamId);
        BDDMockito.verify(teamMapper, BDDMockito.times(1)).toDto(null);

        BDDMockito.verifyNoMoreInteractions(teamClient);
        BDDMockito.verifyNoMoreInteractions(teamMapper);
    }

    @Test
    void givenTeamDtoNotNullWhenExistsThenReturnTrue() {
        TeamDto teamDto = new TeamDto();

        boolean result = teamService.exists(teamDto);

        Assertions.assertTrue(result);
    }

    @Test
    void givenTeamDtoNullWhenExistsThenReturnFalse() {
        boolean result = teamService.exists(null);

        Assertions.assertFalse(result);
    }

    @Test
    void givenUserIsTeamMemberWhenIsTeamMemberThenReturnTrue() {
        String userId = "user123";
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamMemberIds(Arrays.asList("user456", "user123", "user789"));

        boolean result = teamService.isTeamMember(teamDto, userId);

        Assertions.assertTrue(result);
    }

    @Test
    void givenUserIsNotTeamMemberWhenIsTeamMemberThenReturnFalse() {
        String userId = "user123";
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamMemberIds(Arrays.asList("user456", "user789"));

        boolean result = teamService.isTeamMember(teamDto, userId);

        Assertions.assertFalse(result);
    }

    @Test
    void givenTeamDtoIsNullWhenIsTeamMemberThenReturnFalse() {
        String userId = "user123";

        boolean result = teamService.isTeamMember(null, userId);

        Assertions.assertFalse(result);
    }

    @Test
    void givenTeamDtoIdsAreNullWhenIsTeamMemberThenReturnFalse() {
        String userId = "user123";
        TeamDto teamDto = new TeamDto();

        boolean result = teamService.isTeamMember(teamDto, userId);

        Assertions.assertFalse(result);
    }

    @Test
    void givenTeamDtoIdsAreEmptyWhenIsTeamMemberThenReturnFalse() {
        String userId = "user123";
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamMemberIds(Collections.emptyList());

        boolean result = teamService.isTeamMember(teamDto, userId);

        Assertions.assertFalse(result);
    }

    @Test
    void givenUserIdIsNullWhenIsTeamMemberThenReturnFalse() {
        String userId = null;
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamMemberIds(Collections.emptyList());

        boolean result = teamService.isTeamMember(teamDto, userId);

        Assertions.assertFalse(result);
    }

    @Test
    void givenUserIdIsNullAndTeamDtoIsNullWhenIsTeamMemberThenReturnFalse() {
        String userId = null;
        TeamDto teamDto = null;

        boolean result = teamService.isTeamMember(teamDto, userId);

        Assertions.assertFalse(result);
    }

}
