package com.example.lib.Repositories;

import com.example.lib.Sitz;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;


public interface SitzRepository extends CrudRepository<Sitz, Integer>{
    
}
