package com.ecore.roles.controller;

import com.ecore.roles.dto.AssignRoleDto;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(RoleController.class)
class RoleControllerTest {

    private static final String V1_ROLES = "/v1/roles";
    private static final String V1_TEAMS_TEAMID_USERS_USERID_ROLES = "/v1/teams/{team-id}/users/{user-id}/roles";
    private static final String V1_ROLES_ROLEID_TEAMS_USERS = V1_ROLES + "/{role-id}/teams/users";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private EasyRandom generator;

    @MockBean
    private RoleService roleService;

    @MockBean
    private RoleMembershipService roleMembershipService;

    @MockBean
    private RoleMapper roleMapper;

    @MockBean
    private RoleMembershipMapper roleMembershipMapper;

    @MockBean
    private AssignRoleMapper assignRoleMapper;

    @BeforeEach
    void setup() {
        generator = new EasyRandom();
    }

    @Test
    void givenValidRequestWhenCreateRoleThenReturn200Ok() throws Exception {
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("123");

        RoleDto roleDto = new RoleDto();
        RoleResponse response = new RoleResponse();

        BDDMockito.given(roleMapper.toDto(Mockito.any(RoleRequest.class))).willReturn(roleDto);
        BDDMockito.given(roleService.createRole(roleDto)).willReturn(roleDto);
        BDDMockito.given(roleMapper.toResponse(roleDto)).willReturn(response);


        RequestBuilder request = MockMvcRequestBuilders.post(V1_ROLES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleRequest));

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());

        BDDMockito.verify(roleMapper, Mockito.times(1)).toDto(Mockito.any(RoleRequest.class));
        BDDMockito.verify(roleService, Mockito.times(1)).createRole(roleDto);
        BDDMockito.verify(roleMapper, Mockito.times(1)).toResponse(roleDto);

        BDDMockito.verifyNoMoreInteractions(roleMapper);
        BDDMockito.verifyNoMoreInteractions(roleService);
    }

    @Test
    void givenInvalidRequestWhenCreateRoleThenReturn400BadRequest() throws Exception {
        RoleRequest roleRequest = new RoleRequest();

        RequestBuilder request = MockMvcRequestBuilders.post(V1_ROLES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleRequest));

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        BDDMockito.verifyNoInteractions(roleMapper);
        BDDMockito.verifyNoInteractions(roleService);
        BDDMockito.verifyNoInteractions(roleMapper);
    }

    @Test
    void givenValidRequestWhenGetRoleByMembershipThenReturn200Ok() throws Exception {
        String teamId = "123";
        String userId = "123";

        RoleDto roleDto = new RoleDto();
        RoleResponse response = new RoleResponse();

        BDDMockito.given(roleService.findRoleByTeamIdAndUserId(teamId, userId)).willReturn(roleDto);
        BDDMockito.given(roleMapper.toResponse(roleDto)).willReturn(response);

        RequestBuilder request = MockMvcRequestBuilders.get(V1_TEAMS_TEAMID_USERS_USERID_ROLES, teamId, userId)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        BDDMockito.verify(roleService, Mockito.times(1)).findRoleByTeamIdAndUserId(teamId, userId);
        BDDMockito.verify(roleMapper, Mockito.times(1)).toResponse(roleDto);

        BDDMockito.verifyNoMoreInteractions(roleMapper);
        BDDMockito.verifyNoMoreInteractions(roleService);
    }

    @Test
    void givenValidRequestWhenGetMembersByRoleThenReturn200Ok() throws Exception {
        String roleId = "123";

        List<RoleMembershipDto> roleMembershipDtoList = new ArrayList<>();

        List<RoleMembershipResponse> roleMembershipResponseList =
                generator.objects(RoleMembershipResponse.class, 3).toList();

        BDDMockito.given(roleMembershipService.findMembersByRole(roleId)).willReturn(roleMembershipDtoList);
        BDDMockito.given(roleMembershipMapper.toResponseList(roleMembershipDtoList)).willReturn(roleMembershipResponseList);

        RequestBuilder request = MockMvcRequestBuilders.get(V1_ROLES_ROLEID_TEAMS_USERS, roleId)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

        BDDMockito.verify(roleMembershipService, Mockito.times(1)).findMembersByRole(roleId);
        BDDMockito.verify(roleMembershipMapper, Mockito.times(1)).toResponseList(roleMembershipDtoList);

        BDDMockito.verifyNoMoreInteractions(roleMembershipService);
        BDDMockito.verifyNoMoreInteractions(roleMembershipMapper);
    }

    @Test
    void givenValidRequestWhenAssignRoleThenReturn200Ok() throws Exception {
        String roleId = "123";
        String teamId = "123";
        String userId = "123";

        RoleMembershipDto roleMembershipDto = new RoleMembershipDto();
        RoleMembershipResponse roleMembershipResponse = new RoleMembershipResponse();
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest();
        AssignRoleDto assignRoleDto = new AssignRoleDto();

        BDDMockito.given(assignRoleMapper.toDto(BDDMockito.any(AssignRoleRequest.class))).willReturn(assignRoleDto);
        BDDMockito.given(roleMembershipService.assignRole(roleId, teamId, assignRoleDto)).willReturn(roleMembershipDto);
        BDDMockito.given(roleMembershipMapper.toResponse(roleMembershipDto)).willReturn(roleMembershipResponse);

        RequestBuilder request = MockMvcRequestBuilders.post(V1_TEAMS_TEAMID_USERS_USERID_ROLES, roleId, teamId, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignRoleRequest));

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());

        BDDMockito.verify(assignRoleMapper, Mockito.times(1)).toDto(Mockito.any(AssignRoleRequest.class));
        BDDMockito.verify(roleMembershipService, Mockito.times(1)).assignRole(roleId, teamId, assignRoleDto);
        BDDMockito.verify(roleMembershipMapper, Mockito.times(1)).toResponse(roleMembershipDto);

        BDDMockito.verifyNoMoreInteractions(assignRoleMapper);
        BDDMockito.verifyNoMoreInteractions(roleMembershipService);
        BDDMockito.verifyNoMoreInteractions(roleMembershipMapper);
    }

}
