package com.example.lib.Repositories;

import com.example.lib.Benutzer;
import com.example.lib.Bestellung;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BestellungRepository extends CrudRepository<Bestellung, Integer> {
    Bestellung[] findByBenutzer(@Param("benutzer_id")Benutzer benutzer);
}
