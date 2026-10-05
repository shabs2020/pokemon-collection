package com.pokemon.collection.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "ZGV2c2VjcmV0Y2hhbmdlbWVwbGVhc2UzMmNoYXJzbWluaW11bQ");
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", 60L);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtService.generateToken("ash");
        assertNotNull(token);
        assertEquals("ash", jwtService.extractUsername(token));
    }

    @Test
    void extractUsername_ShouldReturnSubject() {
        String token = jwtService.generateToken("ash");
        assertEquals("ash", jwtService.extractUsername(token));
    }

    @Test
    void extractExpiration_ShouldReturnFutureDate() {
        String token = jwtService.generateToken("ash");
        Date expiration = jwtService.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken("ash");
        assertTrue(jwtService.validateToken(token, "ash"));
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidUsername() {
        String token = jwtService.generateToken("ash");
        assertFalse(jwtService.validateToken(token, "misty"));
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", -1L);
        String token = jwtService.generateToken("ash");
        assertFalse(jwtService.validateToken(token, "ash"));
    }
}