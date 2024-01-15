// Import necessary packages
package com.project.firstTry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.firstTry.model.Roles;
import com.project.firstTry.model.Users;

// Repository annotation indicating that this interface is a Spring Data repository
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // Method to find a user by email
    Optional<Users> findByEmail(String email);

    // Method to check if a user with a specific email exists
    Boolean existsByEmail(String email);

    // Method to check if a user with a specific phone number exists
    Boolean existsByPhone(String phone);

    // Method to find a user by role
    Users findByRole(Roles roles);

    // Modifying query to delete a user by its ID
    @Modifying
    @Query("DELETE FROM Users u WHERE u.id_user = :userId")
    void deleteUserById(@Param("userId") long userId);
}
