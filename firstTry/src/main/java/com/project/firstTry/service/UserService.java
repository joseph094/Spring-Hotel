package com.project.firstTry.service;

import com.project.firstTry.dto.UpdatePassword;
import com.project.firstTry.model.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    Users createUser(Users user);

    Users getUserById(Long userId);

    List<Users> getAllUsers();

    Users updateUser(Long userId, Users updatedUser);

    void deleteUser(Long userId);

    UserDetailsService usersDetailsService();

}
