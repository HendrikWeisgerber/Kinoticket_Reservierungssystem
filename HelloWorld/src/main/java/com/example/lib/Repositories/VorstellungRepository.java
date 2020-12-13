package com.example.lib.Repositories;

import com.example.lib.Vorstellung;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VorstellungRepository extends CrudRepository<Vorstellung, Integer> {
    Vorstellung[] findByFilmId(@Param("film_id")int film_id);
}
