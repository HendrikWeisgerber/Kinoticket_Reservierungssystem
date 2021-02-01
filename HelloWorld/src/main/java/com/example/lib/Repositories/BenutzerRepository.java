package com.example.lib.Repositories;

import com.example.lib.Benutzer;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BenutzerRepository extends CrudRepository<Benutzer, Integer>{
    Optional<Benutzer> findByUsername(String username);
}
