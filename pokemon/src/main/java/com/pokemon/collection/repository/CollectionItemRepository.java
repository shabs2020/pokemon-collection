package com.pokemon.collection.repository;

import com.pokemon.collection.domain.CollectionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {
    List<CollectionItem> findByTrainerIdOrderByAddedAtDesc(Long trainerId);
    Optional<CollectionItem> findByIdAndTrainerId(Long id, Long trainerId);
    boolean existsByTrainerIdAndPokemonId(Long trainerId, Integer pokemonId);
}