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
        if (oB.isPresent()) { // TODO Umschreiben? Falls Benutzer nicht gefunden wird -> Fehlermeldung?
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
                if (t.getWarenkorb() != null) {
                    return new ResponseEntity<>("Ticket liegt bereits im Warenkorb", HttpStatus.OK);
                }
                if (t.getKaeufer() != null) {
                    return new ResponseEntity<>("Ticket hat bereits einen anderen Käufer", HttpStatus.OK);
                }
                t.setWarenkorb(benutzer.getWarenkorb());
                ticketRepository.save(t); // TODO debug Internal error 500
                return new ResponseEntity<>(t, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
