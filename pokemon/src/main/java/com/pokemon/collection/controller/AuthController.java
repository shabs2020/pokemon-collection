package com.pokemon.collection.controller;

import com.pokemon.collection.domain.Trainer;
import com.pokemon.collection.repository.TrainerRepository;
import com.pokemon.collection.service.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TrainerRepository trainerRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtService.generateToken(request.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", request.getUsername());
        response.put("expiresIn", 3600);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Token is empty"));
        }
        String username = jwtService.extractUsername(token);

        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Trainer not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("username", trainer.getUsername());
        return ResponseEntity.ok(response);
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;
    }
}
