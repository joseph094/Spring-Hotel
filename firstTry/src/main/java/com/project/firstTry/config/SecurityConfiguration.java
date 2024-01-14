package com.project.firstTry.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.firstTry.model.Roles;
import com.project.firstTry.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserService userService;

    // Configuring the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->request
                        // Allowing access to specific API paths without authentication
                        .requestMatchers("api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("swagger-ui/**")
                        .permitAll()
                        .requestMatchers("v3/api-docs/**")
                        .permitAll()

                        // Restricting access based on roles for certain API paths
                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority(Roles.ADMIN.name())
                        .requestMatchers("/api/v1/user/**").hasAnyAuthority(Roles.USER.name())
                        .requestMatchers("/api/v1/**").hasAnyAuthority(Roles.ADMIN.name(),Roles.USER.name())

                        // Requiring authentication for any other request
                        .anyRequest().authenticated()).sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                         // Configuring custom authentication provider and JWT filter
                        .authenticationProvider(authenicationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );
                return httpSecurity.build();

    }
     // Configuring the authentication provider
    @Bean
    public AuthenticationProvider authenicationProvider(){
        DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.usersDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return  authenticationProvider;
    }
    // Configuring the password encoder (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // Configuring the authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
