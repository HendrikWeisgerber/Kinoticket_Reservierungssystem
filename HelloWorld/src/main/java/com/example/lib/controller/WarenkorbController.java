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
        if (!oB.isEmpty()) { // TODO Umschreiben? Falls Benutzer nicht gefunden wird -> Fehlermeldung?
            b = oB.get();
        } else {
            b = null;
        }
        Warenkorb[] w; //TODO Davit -> Klären warum hier ein Array verwendet wird. 1 zu 1 Beziehung
        if (b != null) {
            w = warenkorbRepository.findByBenutzer(b);
        } else {
        }
        w = new Warenkorb[0];
        Ticket[] t;
        if (w.length > 0) {
            t = ticketRepository.findByWarenkorb(w[0]);
        } else {
            t = new Ticket[0];
        }

        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @RequestMapping(value = "ticket/{ticket_id}", produces = "appliation/json")
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
                    return new ResponseEntity<Object>("Ticket liegt bereits im Warenkorb", HttpStatus.OK);
                }
                if (t.getKaeufer() != null) {
                    return new ResponseEntity<Object>("Ticket hat bereits einen anderen Käufer", HttpStatus.OK);
                }
                t.setWarenkorb(benutzer.getWarenkorb());
                ticketRepository.save(t);
                return new ResponseEntity<Object>(t, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }
}
