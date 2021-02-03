package com.example.lib.Repositories;

import com.example.lib.Benutzer;
import com.example.lib.Warenkorb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WarenkorbRepository extends CrudRepository<Warenkorb, Integer> {
    Warenkorb findByBenutzer(@Param("benutzer_id")Benutzer benutzer);
}
