package com.pokemon.collection.security;

import com.pokemon.collection.domain.Trainer;
import com.pokemon.collection.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TrainerRepository trainerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Trainer not found: " + username));

        return User.builder()
                .username(trainer.getUsername())
                .password(trainer.getPasswordHash())
                .roles("TRAINER")
                .build();
    }
}