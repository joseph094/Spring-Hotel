package com.project.firstTry.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.firstTry.model.Users;
import com.project.firstTry.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling user-related operations by the admin.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UsersController {

    // Injected UserService dependency through constructor (Lombok's @RequiredArgsConstructor)
    private final UserService userService;

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @PostMapping("create")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        // Create a new user using the UserService
        Users createdUser = userService.createUser(user);
        // Return the created user with a 201 Created status
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @GetMapping("find/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
        // Retrieve a user by ID using the UserService
        Users user = userService.getUserById(userId);
        // Return the user if found with a 200 OK status, or a 404 Not Found status otherwise
        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @GetMapping()
    public ResponseEntity<List<Users>> getAllUsers() {
        // Retrieve all users from the UserService
        List<Users> users = userService.getAllUsers();
        // Return the list of users with a 200 OK status
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @PutMapping("/update/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody Users updatedUser) {
        // Update a user by ID using the UserService
        Users updated = userService.updateUser(userId, updatedUser);
        // Return the updated user with a 200 OK status, or a 404 Not Found status otherwise
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints
    @CrossOrigin
    @DeleteMapping("delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        // Delete a user by ID using the UserService
        userService.deleteUser(userId);
        // Return a 204 No Content status indicating successful deletion
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
