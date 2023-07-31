package com.ecore.roles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleMembershipDto {

    private String id;
    private RoleDto roleDto;
    private String teamId;
    private String userId;
    private LocalDateTime creationDate;

}
