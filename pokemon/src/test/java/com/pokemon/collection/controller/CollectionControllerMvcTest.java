package com.pokemon.collection.controller;

import com.pokemon.collection.PokemonCollectionApplication;
import com.pokemon.collection.domain.CollectionItem;
import com.pokemon.collection.domain.Trainer;
import com.pokemon.collection.exception.DuplicateCollectionItemException;
import com.pokemon.collection.repository.TrainerRepository;
import com.pokemon.collection.service.CollectionService;
import com.pokemon.collection.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes = PokemonCollectionApplication.class)
class CollectionControllerMvcTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("springSecurityFilterChain")
    private jakarta.servlet.Filter springSecurityFilterChain;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private CollectionService collectionService;

    @MockitoBean
    private TrainerRepository trainerRepository;

    private MockMvc mockMvc;
    private String bearerToken;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build();
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUsername("ash");
        trainer.setPasswordHash("hash");

        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        bearerToken = "Bearer " + jwtService.generateToken("ash");
    }

    @Test
    void add_ShouldReturnCreatedItem() throws Exception {
        CollectionItem item = new CollectionItem();
        item.setId(1L);
        item.setTrainerId(1L);
        item.setPokemonId(25);
        item.setAddedAt(Instant.now());

        when(collectionService.add(eq("ash"), eq(25))).thenReturn(item);

        mockMvc.perform(post("/api/collection/25")
                        .header("Authorization", bearerToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void list_ShouldReturnItems() throws Exception {
        CollectionItem item = new CollectionItem();
        item.setId(1L);
        item.setTrainerId(1L);
        item.setPokemonId(25);

        when(collectionService.list("ash")).thenReturn(List.of(item));

        mockMvc.perform(get("/api/collection")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void remove_ShouldReturnNoContent() throws Exception {
        doNothing().when(collectionService).remove("ash", 1L);

        mockMvc.perform(delete("/api/collection/1")
                        .header("Authorization", bearerToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void unauthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/collection"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void add_ShouldReturn409ForDuplicatePokemon() throws Exception {
        when(collectionService.add(eq("ash"), eq(25)))
                .thenThrow(new DuplicateCollectionItemException(1L, 25));

        mockMvc.perform(post("/api/collection/25")
                        .header("Authorization", bearerToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.trainerId").value(1))
                .andExpect(jsonPath("$.pokemonId").value(25));
    }

    @Test
    void add_ShouldReturn400ForInvalidPokemonId() throws Exception {
        when(collectionService.add(eq("ash"), eq(0)))
                .thenThrow(new IllegalArgumentException("Pokemon ID must be positive"));

        mockMvc.perform(post("/api/collection/0")
                        .header("Authorization", bearerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void remove_ShouldReturn404ForUnknownItem() throws Exception {
        doThrow(new java.util.NoSuchElementException("Item not found"))
                .when(collectionService).remove("ash", 999L);

        mockMvc.perform(delete("/api/collection/999")
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }
}
