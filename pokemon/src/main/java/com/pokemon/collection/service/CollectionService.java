package com.pokemon.collection.service;

import com.pokemon.collection.domain.CollectionItem;
import com.pokemon.collection.domain.Trainer;
import com.pokemon.collection.exception.DuplicateCollectionItemException;
import com.pokemon.collection.repository.CollectionItemRepository;
import com.pokemon.collection.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionItemRepository collectionItemRepository;
    private final TrainerRepository trainerRepository;

    @Cacheable("trainers")
    public Trainer getTrainerByUsername(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Trainer not found"));
    }

    public CollectionItem add(String username, Integer pokemonId) {
        if (pokemonId <= 0) {
            throw new IllegalArgumentException("Pokemon ID must be positive");
        }
        if (pokemonId > 1025) {
            throw new IllegalArgumentException("Pokemon ID must be <= 1025");
        }

        Trainer trainer = getTrainerByUsername(username);
        Long trainerId = trainer.getId();
        if (collectionItemRepository.existsByTrainerIdAndPokemonId(trainerId, pokemonId)) {
            throw new DuplicateCollectionItemException(trainerId, pokemonId);
        }

        CollectionItem item = new CollectionItem();
        item.setTrainerId(trainerId);
        item.setPokemonId(pokemonId);
        item.setAddedAt(Instant.now());

        try {
            return collectionItemRepository.save(item);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateCollectionItemException(trainerId, pokemonId);
        }
    }

    public List<CollectionItem> list(String username) {
        Trainer trainer = getTrainerByUsername(username);

        return collectionItemRepository.findByTrainerIdOrderByAddedAtDesc(trainer.getId());
    }

    public void remove(String username, Long itemId) {
        Trainer trainer = getTrainerByUsername(username);

        CollectionItem item = collectionItemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
        if (!item.getTrainerId().equals(trainer.getId())) {
            throw new NoSuchElementException("Item not owned by trainer");
        }

        collectionItemRepository.delete(item);
    }
}