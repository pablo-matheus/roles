package com.ecore.roles.repository;

import com.ecore.roles.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String> {

    boolean existsByName(String name);

    Role findByName(String name);

    Role findByRoleMembership_TeamIdAndRoleMembership_UserId(String teamId, String userId);

}
