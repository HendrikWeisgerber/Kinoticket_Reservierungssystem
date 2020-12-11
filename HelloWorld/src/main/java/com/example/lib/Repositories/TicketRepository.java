package com.example.lib.Repositories;

import com.example.lib.Ticket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    Ticket[] findByVorstellungId(@Param("vorstellung_id")int vorstellung_id);
}
