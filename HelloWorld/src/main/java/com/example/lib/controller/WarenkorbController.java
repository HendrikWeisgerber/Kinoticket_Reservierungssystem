package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Repositories.BenutzerRepository;
import com.example.lib.Repositories.TicketRepository;
import com.example.lib.Repositories.WarenkorbRepository;
import com.example.lib.Ticket;
import com.example.lib.Warenkorb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/warenkorb")
public class WarenkorbController {

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    WarenkorbRepository warenkorbRepository;

    @Autowired
    TicketRepository ticketRepository;

    //Nutzer wird zu benutzer
    @RequestMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> getAllTicketsInWarenkorb(Principal principal) {
        Optional<Benutzer> oB = benutzerRepository.findByUsername(principal.getName());
        if (oB.isEmpty()) {
            return new ResponseEntity<>("Kein Benutzer unter diesem Nutzernamen gefunden", HttpStatus.OK);
        } else {
            Ticket[] t = ticketRepository.findByWarenkorb(warenkorbRepository.findByBenutzer(oB.get()));
            return new ResponseEntity<>(t, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/ticket/{ticket_id}", produces = "appliation/json")
    public ResponseEntity<Object> saveTicketInWarenkorb(@PathVariable(value = "ticket_id") long ticket_id,
                                                        Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isPresent()) {
            Benutzer benutzer = optionalBenutzer.get();
            if (benutzer.getWarenkorb() == null) {
                Warenkorb w = new Warenkorb();
                benutzer.setWarenkorb(w);
                benutzerRepository.save(benutzer);
                warenkorbRepository.save(w);
            }

            Optional<Ticket> optionalTicket = ticketRepository.findById((int) ticket_id);
            if (optionalTicket.isPresent()) {
                Ticket t = optionalTicket.get();
                if(t.getWarenkorb()!= null) {
                    return new ResponseEntity<Object>("Ticket ist bereits in einem Warenkorb", HttpStatus.OK);
                }
                if(t.getKaeufer().getId() != benutzer.getId()){
                    return new ResponseEntity<Object>("Ticket ist von einem anderern Kunden reserviert", HttpStatus.OK);
                }
                t.setWarenkorb(benutzer.getWarenkorb());
                //ticketRepository.save(t);
                //TODO throws error 500
                //TODO if fixed, activate test "saveTicketInWarenkorb()" in HelloWorldApplicationTests.java
                //Resolved [org.springframework.http.converter.HttpMessageNotWritableException: No converter for [class com.example.lib.Ticket] with preset Content-Type 'null']
                return new ResponseEntity<Object>(t, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }
}
