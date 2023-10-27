package com.service.question.repositories;

import com.service.question.model.ERole;
import com.service.question.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepo extends JpaRepository<Role, Long> {
    // Role findByUserIdAndRole(Integer userId, String role);
    Optional<Role> findByName(ERole name);
}
