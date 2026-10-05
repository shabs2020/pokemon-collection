package com.pokemon.collection.controller;

import com.pokemon.collection.domain.CollectionItem;
import com.pokemon.collection.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping("/{pokemonId}")
    public ResponseEntity<CollectionItem> add(@PathVariable Integer pokemonId, @AuthenticationPrincipal UserDetails userDetails) {
        CollectionItem item = collectionService.add(userDetails.getUsername(), pokemonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping
    public ResponseEntity<List<CollectionItem>> list(@AuthenticationPrincipal UserDetails userDetails) {
        List<CollectionItem> items = collectionService.list(userDetails.getUsername());
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> remove(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        collectionService.remove(userDetails.getUsername(), itemId);
        return ResponseEntity.noContent().build();
    }
}