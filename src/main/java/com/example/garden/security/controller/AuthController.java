package com.example.garden.security.controller;

import com.example.garden.security.component.JwtUtil;
import com.example.garden.security.dto.AuthResponse;
import com.example.garden.security.model.Role;
import com.example.garden.security.model.User;
import com.example.garden.security.repository.RoleRepository;
import com.example.garden.security.service.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/garden")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController     {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User request) {
        try {
            // Hitelesítés
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Felhasználó betöltése
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            User user = userDetailsService.getByUserName(userDetails.getUsername());
            // Token generálás
            String token = jwtUtil.generateToken(userDetails, user);
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        userDetailsService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }


}
