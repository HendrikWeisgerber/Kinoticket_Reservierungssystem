package com.example.lib.Repositories;

import com.example.lib.Kinosaal;
import com.example.lib.Sitz;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface SitzRepository extends CrudRepository<Sitz, Integer>{
    Sitz[] findByKinosaalId(@Param("kinosaal_id") int kinosaal_Id);
}
