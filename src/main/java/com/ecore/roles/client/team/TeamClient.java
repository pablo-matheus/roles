package com.ecore.roles.client.team;

import com.ecore.roles.client.team.response.TeamResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "${clients.team.name}", url = "${clients.team.url}")
public interface TeamClient {

    @GetMapping("/teams/{id}")
    TeamResponse getById(@PathVariable String id);

}
