package com.ecore.roles.client.user;

import com.ecore.roles.client.user.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "${clients.user.name}", url = "${clients.user.url}")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserResponse getById(@PathVariable String id);

}
