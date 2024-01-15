package com.project.firstTry.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.project.firstTry.model.Users;

public interface UserService {

    // Method to create a new user
    Users createUser(Users user);

    // Method to retrieve a user by ID
    Users getUserById(Long userId);

    // Method to get all users
    List<Users> getAllUsers();

    // Method to update user information
    Users updateUser(Long userId, Users updatedUser);

    // Method to delete a user
    void deleteUser(Long userId);

    // Method to provide UserDetailsService for authentication purposes
    UserDetailsService usersDetailsService();
}
