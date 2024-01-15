// Import necessary packages
package com.project.firstTry.dto;

import lombok.Data;

// Lombok annotation to automatically generate getters, setters, toString, and other boilerplate code
@Data
public class JwtAuthenticationResponse {

    // Private field to store the JWT token
    private String token;

    // Private field to store the refresh token
    private String refreshToken;
}
