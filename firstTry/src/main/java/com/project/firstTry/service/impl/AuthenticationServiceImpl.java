// Import necessary packages
package com.project.firstTry.service.impl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.firstTry.dto.JwtAuthenticationResponse;
import com.project.firstTry.dto.RefreshTokenRequest;
import com.project.firstTry.dto.SignUpRequest;
import com.project.firstTry.dto.SigninRequest;
import com.project.firstTry.dto.UpdatePassword;
import com.project.firstTry.model.Roles;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.UsersRepository;
import com.project.firstTry.service.AuthenticationService;
import com.project.firstTry.service.JWTService;

import lombok.RequiredArgsConstructor;

// Service annotation indicating that this class is a Spring service
@Service
// Lombok annotation to generate a constructor with required arguments
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    // Injecting UsersRepository dependency
    private final UsersRepository usersRepository;

    // Injecting PasswordEncoder dependency
    private final PasswordEncoder passwordEncoder;

    // Injecting AuthenticationManager dependency
    private final AuthenticationManager authenticationManager;

    // Injecting JWTService dependency
    private final JWTService jwtService;

    // Method to handle user signup
    public ResponseEntity<String> signup(SignUpRequest signUpRequest) {
        // Check if email already exists
        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists");
        }
        // Check if phone number already exists
        if (usersRepository.existsByPhone(signUpRequest.getPhone())) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Phone number already exists");
        }

        // Create a new user
        Users user = new Users();
        user.setEmail(signUpRequest.getEmail());
        user.setFirst_name(signUpRequest.getFirst_name());
        user.setLast_name(signUpRequest.getLast_name());
        user.setPhone(signUpRequest.getPhone());
        user.setRole(Roles.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Save the user to the repository
        Users savedUser = usersRepository.save(user);

        // Return a success response
        return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully");
    }

    // Method to handle user signin
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        // Authenticate user credentials
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        // Retrieve user details by email
        var user = usersRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid Email or Password"));

        // Generate JWT token and refresh token
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        // Create and return JwtAuthenticationResponse
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    // Method to handle refreshing JWT token
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // Extract user email from the token
        String userEmail = jwtService.exctractEmail(refreshTokenRequest.getToken());

        // Retrieve user by email
        Users user = usersRepository.findByEmail(userEmail).orElseThrow();

        // Check if the provided token is valid
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            // Generate a new JWT token
            var jwt = jwtService.generateToken(user);

            // Create and return JwtAuthenticationResponse
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }

        // Return null if the token is not valid
        return null;
    }

    // Method to retrieve user details from the JWT token
    public Users getUserFromToken(String token) {
        String userEmail = jwtService.exctractEmail(token);
        return usersRepository.findByEmail(userEmail).orElse(null);
    }

    // Method to update user password
    @Override
    public Users updatePasswordUser(Long userId, UpdatePassword updatePassword) {
        // Retrieve user by ID
        Optional<Users> optionalUser = usersRepository.findById(userId);

        // Check if the user exists
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();

            // Update user password
            existingUser.setPassword(passwordEncoder.encode(updatePassword.getNewpassword()));

            // Save the updated user
            return usersRepository.save(existingUser);
        }

        // Return null if the user does not exist
        return null;
    }
}
