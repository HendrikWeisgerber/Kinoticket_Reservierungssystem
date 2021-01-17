package com.example.lib.security;

import com.example.lib.Benutzer;
import com.example.lib.Repositories.BenutzerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BenutzerServiceImpl implements IBenutzuerSrvice{

    private BenutzerRepository benutzerRepository;

    public BenutzerServiceImpl(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    @Override
    public Optional<Benutzer> findById(int id) {
        return benutzerRepository.findById(id);
    }

    @Override
    public Benutzer save(Benutzer benutzer) {
        return benutzerRepository.save(benutzer);
    }

    @Override
    public Iterable<Benutzer> findAll() {
        return benutzerRepository.findAll();
    }
}

