package com.project.firstTry.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.firstTry.model.Users;
import com.project.firstTry.repository.ReservationRepository;
import com.project.firstTry.repository.UsersRepository;
import com.project.firstTry.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // Injecting necessary repositories using Lombok's @RequiredArgsConstructor
    private final UsersRepository usersRepository;
    private final ReservationRepository reservationRepository;

    // Method to create a new user
    @Override
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    // Method to retrieve a user by ID
    @Override
    public Users getUserById(Long userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    // Method to get all users
    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // Method to update user information
    public Users updateUser(Long userId, Users updatedUser) {
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
            // Update user fields based on your requirements
            existingUser.setFirst_name(updatedUser.getFirst_name());
            existingUser.setLast_name(updatedUser.getLast_name());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            System.out.println("updating User");

            return usersRepository.save(existingUser);
        }
        return null; // If user with given ID is not found
    }

    // Method to delete a user
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // Delete reservations associated with the user
        reservationRepository.deleteByUserId(userId);

        // Delete the user
        usersRepository.deleteById(userId);
    }

    // Method to load user details by username (used for authentication)
    @Override
    public UserDetailsService usersDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                // Find user by email and throw an exception if not found
                return usersRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            }
        };
    }
}
