package com.pokemon.collection.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    indexes = {
        @Index(name = "idx_collection_trainer_added", columnList = "trainerId, addedAt DESC"),
        @Index(name = "idx_collection_trainer_item", columnList = "trainerId, id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uc_collection_trainer_pokemon", columnNames = {"trainerId", "pokemonId"})
    }
)
@Getter
@Setter
public class CollectionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long trainerId;

    @NotNull
    private Integer pokemonId;

    private Instant addedAt;
}
