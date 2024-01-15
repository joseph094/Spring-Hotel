package com.project.firstTry.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    // Method to extract email from JWT token
    String exctractEmail(String token);

    // Method to generate a JWT token based on user details
    String generateToken(UserDetails userDetails);

    // Method to check if a JWT token is valid for a given user
    boolean isTokenValid(String token, UserDetails userDetails);

    // Method to generate a refresh token with extra claims and user details
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
