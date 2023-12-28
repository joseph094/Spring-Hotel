package com.project.firstTry.service.impl;

import com.project.firstTry.dto.*;
import com.project.firstTry.model.Roles;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.UsersRepository;
import com.project.firstTry.service.AuthenticationService;
import com.project.firstTry.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private  final AuthenticationManager authenticationManager;

    private final JWTService jwtService;
    public ResponseEntity<String> signup(SignUpRequest signUpRequest) {
        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists");
        }
        if (usersRepository.existsByPhone(signUpRequest.getPhone())) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Phone number already exists");
        }


        Users user = new Users();

        user.setEmail(signUpRequest.getEmail());
        user.setFirst_name(signUpRequest.getFirst_name());
        user.setLast_name(signUpRequest.getLast_name());
        user.setPhone(signUpRequest.getPhone());
        user.setRole(Roles.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Users savedUser = usersRepository.save(user);

        // Return the created user
        return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfuly ");
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
        var user = usersRepository.findByEmail(signinRequest.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid Email or pass"));
        var jwt =jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;


    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){

        String userEmail =jwtService.exctractEmail(refreshTokenRequest.getToken());
        Users user = usersRepository.findByEmail(userEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt =jwtService.generateToken(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;

        }
        return null;
    }
    public Users getUserFromToken(String token) {
        String userEmail = jwtService.exctractEmail(token);
        return usersRepository.findByEmail(userEmail).orElse(null);
    }
    @Override

    public Users updatePasswordUser(Long userId, UpdatePassword updatePassword){
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
            // Update user fields based on your requirements
            existingUser.setPassword(passwordEncoder.encode(updatePassword.getNewpassword()));
            return usersRepository.save(existingUser);
        }
        return null;
    }


}
