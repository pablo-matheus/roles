package com.ecore.roles.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleMembershipResponse {

    private String id;

    @JsonProperty("role")
    private RoleResponse roleResponse;

    private String teamId;

    private String userId;

    private LocalDateTime creationDate;

}
