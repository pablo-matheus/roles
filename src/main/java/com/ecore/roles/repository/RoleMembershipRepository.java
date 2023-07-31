package com.ecore.roles.repository;

import com.ecore.roles.entity.RoleMembership;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleMembershipRepository extends CrudRepository<RoleMembership, String> {

    List<RoleMembership> findByRoleId(String roleId);

    boolean existsByTeamIdAndUserId(String teamId, String userId);

}
