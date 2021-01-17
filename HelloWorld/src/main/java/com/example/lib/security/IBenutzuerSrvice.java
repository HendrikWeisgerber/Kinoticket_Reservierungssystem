package com.example.lib.security;

import com.example.lib.Benutzer;

import java.util.Optional;

public interface IBenutzuerSrvice {
    Optional<Benutzer> findById(int id);
    Benutzer save(Benutzer benutzer);
    Iterable<Benutzer> findAll();

}
