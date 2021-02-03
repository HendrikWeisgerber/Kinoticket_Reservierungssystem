package com.example.lib.Repositories;

import com.example.lib.Snack;
import com.example.lib.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SnackRepository extends CrudRepository<Snack, Integer>{
    Snack findByTicket(@Param("ticket_id") Ticket ticket);
}
