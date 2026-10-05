package com.pokemon.collection.service;

import com.pokemon.collection.domain.CollectionItem;
import com.pokemon.collection.domain.Trainer;
import com.pokemon.collection.repository.CollectionItemRepository;
import com.pokemon.collection.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionServiceTest {

    @Mock
    private CollectionItemRepository collectionItemRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private CollectionService collectionService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUsername("ash");
        trainer.setPasswordHash("hash");
    }

    @Test
    void add_ShouldSaveItem() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(collectionItemRepository.save(any(CollectionItem.class))).thenAnswer(invocation -> {
            CollectionItem item = invocation.getArgument(0);
            item.setId(1L);
            return item;
        });

        CollectionItem item = collectionService.add("ash", 25);
        assertEquals(1L, item.getTrainerId());
        assertEquals(25, item.getPokemonId());
        assertNotNull(item.getAddedAt());
    }

    @Test
    void add_ShouldThrowForInvalidPokemonId() {
        assertThrows(IllegalArgumentException.class, () -> collectionService.add("ash", 0));
    }

    @Test
    void add_ShouldThrowForUnknownTrainer() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> collectionService.add("ash", 25));
    }

    @Test
    void list_ShouldReturnItems() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(collectionItemRepository.findByTrainerIdOrderByAddedAtDesc(1L))
                .thenReturn(List.of(new CollectionItem()));

        List<CollectionItem> items = collectionService.list("ash");
        assertEquals(1, items.size());
    }

    @Test
    void list_ShouldThrowForUnknownTrainer() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> collectionService.list("ash"));
    }

    @Test
    void remove_ShouldDeleteItem() {
        CollectionItem item = new CollectionItem();
        item.setId(1L);
        item.setTrainerId(1L);

        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(collectionItemRepository.findById(1L)).thenReturn(Optional.of(item));

        collectionService.remove("ash", 1L);
        verify(collectionItemRepository, times(1)).delete(item);
    }

    @Test
    void remove_ShouldThrowForUnknownItem() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(collectionItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> collectionService.remove("ash", 1L));
    }
}