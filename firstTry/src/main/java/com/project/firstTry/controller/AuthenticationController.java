package com.project.firstTry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.firstTry.dto.JwtAuthenticationResponse;
import com.project.firstTry.dto.RefreshTokenRequest;
import com.project.firstTry.dto.SignUpRequest;
import com.project.firstTry.dto.SigninRequest;
import com.project.firstTry.dto.UpdatePassword;
import com.project.firstTry.model.Users;
import com.project.firstTry.service.AuthenticationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    // Enable Cross-Origin Resource Sharing (CORS) for all endpoints in this controller
    @CrossOrigin

    @PostMapping("/signup")
    public ResponseEntity<ResponseEntity<String>> signup(@RequestBody SignUpRequest signUpRequest){
      // Call the AuthenticationService to handle the signup request and return the response
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }
    // Enable Cross-Origin Resource Sharing (CORS) for the "/api/v1/auth/signin" endpoint
    @CrossOrigin

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest){
        // Call the AuthenticationService to handle the signin request and return the response
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }
        // Endpoint for refreshing the JWT token
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        // Call the AuthenticationService to handle the token refresh request and return the response
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
        // Enable Cross-Origin Resource Sharing (CORS) for the "/api/v1/auth/profil" endpoint

    @CrossOrigin

    @GetMapping("/profil")
    public ResponseEntity<Users> getUserFromToken(@RequestHeader("Authorization") String token) {
        // Extract the token from the "Authorization" header, assuming it's in the format "Bearer <token>"
        String actualToken = extractTokenFromHeader(token);

        if (actualToken != null) {
             // Call the AuthenticationService to get the user based on the token
            Users user = authenticationService.getUserFromToken(actualToken);
            // Handle the case where the user is not found for the given token

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                // Handle the case where the token is valid but the user is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            // Handle the case where the token is not provided or in an invalid format
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Enable Cross-Origin Resource Sharing (CORS) for the "/api/v1/auth/users/updatepassword/{userId}" endpoint
    @CrossOrigin
    @PostMapping("users/updatepassword/{userId}")
    public ResponseEntity<Users> updatePassword(@PathVariable long userId,  @RequestBody UpdatePassword updatePassword) {
        // Call the AuthenticationService to update the user's password
        Users updatedUser = authenticationService.updatePasswordUser(userId,updatePassword);
        // Return the updated user with a 201 CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    }
    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}
