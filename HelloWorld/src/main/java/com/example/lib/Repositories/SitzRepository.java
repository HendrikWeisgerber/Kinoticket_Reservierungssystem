package com.example.lib.Repositories;

import com.example.lib.Kinosaal;
import com.example.lib.Sitz;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface SitzRepository extends CrudRepository<Sitz, Integer>{
    com.example.lib.Sitz[] findByKinosaal(@Param("kinosaal") Kinosaal kinosaal);
}
