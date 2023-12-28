package com.project.firstTry.controller;

import com.project.firstTry.dto.*;
import com.project.firstTry.model.Users;
import com.project.firstTry.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @CrossOrigin

    @PostMapping("/signup")
    public ResponseEntity<ResponseEntity<String>> signup(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }
    @CrossOrigin

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest){
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
    @CrossOrigin

    @GetMapping("/profil")
    public ResponseEntity<Users> getUserFromToken(@RequestHeader("Authorization") String token) {
        // Extract the token from the "Authorization" header, assuming it's in the format "Bearer <token>"
        String actualToken = extractTokenFromHeader(token);

        if (actualToken != null) {
            Users user = authenticationService.getUserFromToken(actualToken);

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


    @CrossOrigin
    @PostMapping("users/updatepassword/{userId}")
    public ResponseEntity<Users> updatePassword(@PathVariable long userId,  @RequestBody UpdatePassword updatePassword) {
        Users updatedUser = authenticationService.updatePasswordUser(userId,updatePassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    }
    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}
