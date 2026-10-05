package com.pokemon.collection.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (String password : args) {
            System.out.println("Password: " + password + " -> Hash: " + encoder.encode(password));
        }
    }
}