package com.project.firstTry.service;

import org.springframework.http.ResponseEntity;

import com.project.firstTry.dto.JwtAuthenticationResponse;
import com.project.firstTry.dto.RefreshTokenRequest;
import com.project.firstTry.dto.SignUpRequest;
import com.project.firstTry.dto.SigninRequest;
import com.project.firstTry.dto.UpdatePassword;
import com.project.firstTry.model.Users;

public interface AuthenticationService {

    // Method for user registration
    ResponseEntity<String> signup(SignUpRequest signUpRequest);

    // Method for user authentication and obtaining JWT token
    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    // Method for refreshing JWT token
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    // Method to retrieve user details from JWT token
    Users getUserFromToken(String token);

    // Method to update user password
    Users updatePasswordUser(Long userId, UpdatePassword updatePassword);
}
