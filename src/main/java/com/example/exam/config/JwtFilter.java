package com.example.exam.config;

import com.example.exam.repository.EmployeeRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final EmployeeRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        // Check for Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // remove "Bearer "
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT expired: " + e.getMessage());
            } catch (Exception e) {
                logger.error("JWT error: " + e.getMessage());
            }
        }

        // Set security context if email is valid and not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent() && jwtUtil.validateToken(jwt, new User(email, userOptional.get().getPassword(), java.util.List.of()))) {
                String role = jwtUtil.extractRole(jwt);
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

                var userDetails = new User(email, userOptional.get().getPassword(), authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
