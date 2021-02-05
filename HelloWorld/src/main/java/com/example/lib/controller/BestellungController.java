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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
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

    @RequestMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> getAllTicketsInBestellung(Principal principal) {

        Optional<Benutzer> oB = benutzerRepository.findByUsername(principal.getName());
        Benutzer b;
        if (oB.isPresent()) {
            b = oB.get();
            Bestellung[] bestellungen;
            bestellungen = bestellungRepository.findByBenutzer(b);
            Ticket[][] t = new Ticket[bestellungen.length][];
            if(bestellungen.length > 0){
                for(int i = 0;i < bestellungen.length; i++){
                    t[i] = ticketRepository.findByBestellung(bestellungen[i]);
                    for(Ticket ticket : t[i]){
                        ticket.updatePreis();
                    }
                }

                return new ResponseEntity<>(t, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Dieser Benutzer hat keine Bestellung", HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>("Kein Benutzer unter diesem Benutzernamen gefunden", HttpStatus.OK);
        }

    }
}
