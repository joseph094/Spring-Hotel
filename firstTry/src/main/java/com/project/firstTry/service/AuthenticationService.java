package com.project.firstTry.service;

import com.project.firstTry.dto.*;
import com.project.firstTry.model.Users;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<String> signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    Users getUserFromToken(String token);
    Users updatePasswordUser(Long userId, UpdatePassword updatePassword);
}
