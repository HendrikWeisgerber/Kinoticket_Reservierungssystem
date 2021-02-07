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
        Benutzer b;
        if (oB.isPresent()) {
            b = oB.get();
        } else {
            return new ResponseEntity<>("Kein Benutzer gefunden", HttpStatus.OK);
        }
        Optional<Warenkorb> optionalWarenkorb = warenkorbRepository.findByBenutzer(b);
        if (optionalWarenkorb.isEmpty()) return new ResponseEntity<>("Kein Warenkorb", HttpStatus.OK);
        Warenkorb warenkorb = optionalWarenkorb.get();
        Ticket[] tickets = ticketRepository.findByWarenkorb(warenkorb);
        if (tickets.length < 1) return new ResponseEntity<>("Warenkorb leer", HttpStatus.OK);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @RequestMapping(value = "/ticket/{ticket_id}", produces = "application/json")
    public ResponseEntity<Object> saveTicketInWarenkorb(@PathVariable(value = "ticket_id") long ticket_id,
                                                        Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        System.out.println(principal.getName());
        if (optionalBenutzer.isEmpty()) {
            return new ResponseEntity<>("Benutzer nicht gefunden", HttpStatus.OK);
        }
        Benutzer benutzer = optionalBenutzer.get();

        if (benutzer.getWarenkorb() == null) {
            Warenkorb w = new Warenkorb();
            benutzer.setWarenkorb(w);
            benutzerRepository.save(benutzer);
            warenkorbRepository.save(w);
        }

        Optional<Ticket> optionalTicket = ticketRepository.findById((int) ticket_id);

        if (optionalTicket.isEmpty()) {
            return new ResponseEntity<>("Ticket nicht gefunden", HttpStatus.OK);
        }
        Ticket t = optionalTicket.get();
        if (t.getWarenkorb() != null) {
            return new ResponseEntity<>("Ticket istbereits  in einem Warenkorb", HttpStatus.OK);
        }
        if (t.getKaeufer().getId() != benutzer.getId()) {
            return new ResponseEntity<>("Ticket ist von einem anderern Kunden reserviert", HttpStatus.OK);
        }
        t.setWarenkorb(benutzer.getWarenkorb());
        ticketRepository.save(t);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @RequestMapping(value = "remove/ticket/{ticket_id}", produces = "application/json")
    public ResponseEntity<Object> removeTicketInWarenkorb(@PathVariable(value = "ticket_id") long ticket_id,
                                                        Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        System.out.println(principal.getName());
        if (optionalBenutzer.isEmpty()) {
            return new ResponseEntity<>("Benutzer nicht gefunden", HttpStatus.OK);
        }
        Benutzer benutzer = optionalBenutzer.get();
        Optional<Warenkorb> warenkorbOptional = warenkorbRepository.findByBenutzer(benutzer);
        if (warenkorbOptional.isEmpty()) {
            return new ResponseEntity<>("Kein Warenkorb für den Benutzer gefunden", HttpStatus.OK);
        }

        Warenkorb warenkorb = warenkorbOptional.get();

        Optional<Ticket> optionalTicket = ticketRepository.findById((int) ticket_id);

        if (optionalTicket.isEmpty()) {
            return new ResponseEntity<>("Ticket nicht gefunden", HttpStatus.OK);
        }
        Ticket t = optionalTicket.get();
        if (t.getWarenkorb() == null || t.getWarenkorb().getId() != warenkorb.getId()) {
            return new ResponseEntity<>("Ticket befindet sich nicht in dem Warenkorb des Benutzers", HttpStatus.OK);
        }

        if (t.getKaeufer().getId() != benutzer.getId()) {
            return new ResponseEntity<>("Ticket ist von einem anderern Kunden reserviert", HttpStatus.OK);
        }

        t.setWarenkorb(null);
        ticketRepository.delete(t);
        return new ResponseEntity<>("Ticket entfernt", HttpStatus.OK);
    }

}
