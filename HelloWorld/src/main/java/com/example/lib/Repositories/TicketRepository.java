package com.example.lib.Repositories;

import com.example.lib.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    Ticket[] findByVorstellungIdAndSitzId(@Param("vorstellung_id")int vorstellung_id,
                                          @Param("ticket_id")int sitz_id);
    Ticket[] findByVorstellung(@Param("vorstellung_id")Vorstellung vorstellung);
    Ticket[] findByKaeufer(@Param("kaeufer_id")Benutzer kaeufer);
    Ticket[] findByWarenkorb(@Param("warenkorb_id")Warenkorb warenkorb);
    Ticket[] findByBestellung(@Param("bestellung_id")Bestellung bestellung);
    Ticket[] findBySitz(@Param("sitz_id")Sitz sitz);

}
