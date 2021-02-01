package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Bestellung;
import com.example.lib.Repositories.BenutzerRepository;
import com.example.lib.Repositories.BestellungRepository;
import com.example.lib.Repositories.TicketRepository;
import com.example.lib.Repositories.WarenkorbRepository;
import com.example.lib.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/bestellung")
public class BestellungController {

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    BestellungRepository bestellungRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    WarenkorbRepository warenkorbRepository;

    @RequestMapping(value = "/nutzer/{nutzer_id}", produces = "application/json")
    public ResponseEntity<Object> getAllTicketsInBestellung(@PathVariable(value = "nutzer_id") long nutzer_id) {

        Optional<Benutzer> oB = benutzerRepository.findById((int) nutzer_id);
        Benutzer b;
        if (!oB.isEmpty()) {
            b = oB.get();
        } else {
            b = null;
        }
        Bestellung[] bestellung;
        if (b != null) {
            bestellung = bestellungRepository.findByBenutzer(b);
        } else {
            bestellung = new Bestellung[0];
        }
        Ticket[] t;
        if (bestellung.length > 0) {
            t = ticketRepository.findByBestellung(bestellung[0]);
        } else {
            t = new Ticket[0];
        }
        return new ResponseEntity<>(t, HttpStatus.OK);
    }
}
