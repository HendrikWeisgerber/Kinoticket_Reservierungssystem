package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Repositories.BenutzerRepository;
import com.example.lib.Repositories.SnackRepository;
import com.example.lib.Repositories.TicketRepository;
import com.example.lib.Repositories.WarenkorbRepository;
import com.example.lib.Snack;
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
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/snack")
public class SnackController {

    @Autowired
    SnackRepository snackRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    WarenkorbRepository warenkorbRepository;

    @Autowired
    TicketRepository ticketRepository;

    @RequestMapping(value = "/ticket/{ticket_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> getSnackByTicket(@PathVariable(value = "ticket_id") long ticket_id,
                                                    Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Kein Beutzer", HttpStatus.OK);
        Benutzer benutzer = optionalBenutzer.get();

        Optional<Ticket> optionalTicket = ticketRepository.findById((int) ticket_id);
        if (optionalTicket.isEmpty()) return new ResponseEntity<>("Kein Ticket", HttpStatus.OK);
        Ticket ticket = optionalTicket.get();

        if (ticket.getKaeufer().getId() != benutzer.getId()) return new ResponseEntity<>("Anderer Käufer", HttpStatus.OK);

        return new ResponseEntity<>(ticket.getSnack(), HttpStatus.OK);
    }

    @RequestMapping(value = "/warenkorb}", produces = "application/json", method = POST)
    public ResponseEntity<Object> getSnackByWarenkorn(Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Kein Beutzer", HttpStatus.OK);
        Benutzer benutzer = optionalBenutzer.get();

        Optional<Warenkorb> optionalWarenkorb = warenkorbRepository.findByBenutzer(benutzer);
        if (optionalWarenkorb.isEmpty()) return new ResponseEntity<>("Kein Warenkorb", HttpStatus.OK);
        Warenkorb warenkorb = optionalWarenkorb.get();

        if (warenkorb.getBenutzer().getId() != benutzer.getId()) return new ResponseEntity<>("Anderer Käufer", HttpStatus.OK);

        Ticket[] tickets = ticketRepository.findByWarenkorb(warenkorb);
        if (tickets.length < 1) return new ResponseEntity<>("Warenkorb leer", HttpStatus.OK);
        ArrayList<Snack> snacks = new ArrayList<>();
        for (Ticket ticket: tickets) {
            snacks.add(ticket.getSnack());
        }

        return new ResponseEntity<>(snacks, HttpStatus.OK);
    }
}
