package com.example.lib.Repositories;

import com.example.lib.Benutzer;
import com.example.lib.Bestellung;
import com.example.lib.Sitz;
import com.example.lib.Ticket;
import com.example.lib.Vorstellung;
import com.example.lib.Warenkorb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    Ticket[] findByVorstellungId(@Param("vorstellung_id")int vorstellung_id);
    Ticket[] findByVorstellung(@Param("vorstellung_id")Vorstellung vorstellung);
    Ticket[] findByKaeufer(@Param("kaeufer_id")Benutzer kaeufer);
    Ticket[] findByWarenkorb(@Param("warenkorb_id")Warenkorb warenkorb);
    Ticket[] findByBestellung(@Param("bestellung_id")Bestellung bestellung);
    Ticket[] findBySitz(@Param("sitz_id")Sitz sitz);

}
