package com.example.lib.Repositories;

import com.example.lib.Benutzer;

import org.springframework.data.repository.CrudRepository;

public interface BenutzerRepository extends CrudRepository<Benutzer, Integer>{
    Benutzer findByUsername(String username);
}
