// Import necessary packages
package com.project.firstTry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.firstTry.model.Role;

// Repository interface for accessing Role entities
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Method to find a role by its name
    Optional<Role> findByName(String name);
}
