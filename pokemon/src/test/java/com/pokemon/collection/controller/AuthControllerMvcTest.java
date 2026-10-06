package com.pokemon.collection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.collection.PokemonCollectionApplication;
import com.pokemon.collection.domain.Trainer;
import com.pokemon.collection.repository.TrainerRepository;
import com.pokemon.collection.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes = PokemonCollectionApplication.class)
class AuthControllerMvcTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("springSecurityFilterChain")
    private jakarta.servlet.Filter springSecurityFilterChain;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TrainerRepository trainerRepository;

    private MockMvc mockMvc;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build();
        trainer = new Trainer();
        trainer.setUsername("ash");
        trainer.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq3J5v4uS5sV5z8e.5dKX5s5s5s5sO");
    }

    @Test
    void login_ShouldReturnToken() throws Exception {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setUsername("ash");
        request.setPassword("pikachu123");

        Authentication authentication = new UsernamePasswordAuthenticationToken("ash", "pikachu123");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("ash"));
    }

    @Test
    void login_ShouldReturn401ForInvalidCredentials() throws Exception {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setUsername("ash");
        request.setPassword("wrong");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_ShouldReturn400WhenUsernameMissing() throws Exception {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setPassword("pikachu123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturn400WhenPasswordMissing() throws Exception {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setUsername("ash");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturn415WhenContentTypeMissing() throws Exception {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setUsername("ash");
        request.setPassword("pikachu123");

        mockMvc.perform(post("/api/auth/login")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType());
    }
}
