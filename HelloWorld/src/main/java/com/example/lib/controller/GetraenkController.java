package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Getraenk;
import com.example.lib.Repositories.BenutzerRepository;
import com.example.lib.Repositories.GetraenkRepository;
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
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/getraenk")
public class GetraenkController {

    @Autowired
    GetraenkRepository getraenkRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    WarenkorbRepository warenkorbRepository;

    @Autowired
    TicketRepository ticketRepository;

    @RequestMapping(value = "", produces = "application/json", method = GET)
    public ResponseEntity<Object> getAllSnacks() {
        return getraenkRepository.findAll() == null ? new ResponseEntity<>("Keine Getränke", HttpStatus.OK)
                : new ResponseEntity<>(getraenkRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/ticket/{ticket_id}", produces = "application/json", method = GET)
    public ResponseEntity<Object> getGetraenkByTicket(@PathVariable(value = "ticket_id") long ticket_id,
                                                   Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Kein Beutzer", HttpStatus.OK);
        Benutzer benutzer = optionalBenutzer.get();

        Optional<Ticket> optionalTicket = ticketRepository.findById((int) ticket_id);
        if (optionalTicket.isEmpty()) return new ResponseEntity<>("Kein Ticket", HttpStatus.OK);
        Ticket ticket = optionalTicket.get();

        if (ticket.getKaeufer().getId() != benutzer.getId()) return new ResponseEntity<>("Anderer Käufer", HttpStatus.OK);

        return new ResponseEntity<>(ticket.getGetraenk(), HttpStatus.OK);
    }

    @RequestMapping(value = "/warenkorb", produces = "application/json", method = GET)
    public ResponseEntity<Object> getGetraenkByWarenkorb(Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Kein Beutzer", HttpStatus.OK);
        Benutzer benutzer = optionalBenutzer.get();

        Optional<Warenkorb> optionalWarenkorb = warenkorbRepository.findByBenutzer(benutzer);
        if (optionalWarenkorb.isEmpty()) return new ResponseEntity<>("Kein Warenkorb", HttpStatus.OK);
        Warenkorb warenkorb = optionalWarenkorb.get();

        if (warenkorb.getBenutzer().getId() != benutzer.getId()) return new ResponseEntity<>("Anderer Käufer", HttpStatus.OK);

        Ticket[] tickets = ticketRepository.findByWarenkorb(warenkorb);
        if (tickets.length < 1) return new ResponseEntity<>("Warenkorb leer", HttpStatus.OK);
        ArrayList<Getraenk> getraenke = new ArrayList<>();
        for (Ticket ticket: tickets) {
            getraenke.add(ticket.getGetraenk());
        }

        return new ResponseEntity<>(getraenke, HttpStatus.OK);
    }
}