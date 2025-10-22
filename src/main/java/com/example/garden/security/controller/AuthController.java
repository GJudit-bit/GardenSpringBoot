package com.example.garden.security.controller;

import com.example.garden.security.component.JwtUtil;
import com.example.garden.security.dto.AuthResponse;
import com.example.garden.security.model.User;
import com.example.garden.security.service.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            User user = userDetailsService.getByUserName(userDetails.getUsername());
            String token = jwtUtil.generateToken(userDetails, user);
            return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        userDetailsService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }


}
