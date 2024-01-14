package com.project.firstTry.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.firstTry.service.JWTService;
import com.project.firstTry.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the Authorization header from the incoming request
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Check if the Authorization header is empty or does not start with "Bearer "
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            // If not, continue with the filter chain without performing JWT authentication
            filterChain.doFilter(request, response);
            return;
        }
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.substring(7); // Remove "Bearer " prefix
        String userEmail = jwtService.exctractEmail(jwt);

        // Check if the user email is not empty and no authentication is currently present in the SecurityContext
        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Load user details based on the extracted email
                UserDetails userDetails = userService.usersDetailsService().loadUserByUsername(userEmail);
                // Validate the JWT token against the user details
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // If valid, create an authentication token and set it in the SecurityContext
                    UsernamePasswordAuthenticationToken token =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (Exception e) {
                // Handle exceptions appropriately, e.g., log or send an error response
                logger.error("Exception during JWT authentication: " + e.getMessage(), e);
            }
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
