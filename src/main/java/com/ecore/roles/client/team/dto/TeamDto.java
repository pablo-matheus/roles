package com.ecore.roles.client.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {

    private String id;
    private String name;
    private String teamLeadId;
    private List<String> teamMemberIds;

}
