package com.pokemon.collection.exception;

import lombok.Getter;

@Getter
public class DuplicateCollectionItemException extends RuntimeException {
    private final Long trainerId;
    private final Integer pokemonId;

    public DuplicateCollectionItemException(Long trainerId, Integer pokemonId) {
        super("Pokemon already exists in collection");
        this.trainerId = trainerId;
        this.pokemonId = pokemonId;
    }
}
