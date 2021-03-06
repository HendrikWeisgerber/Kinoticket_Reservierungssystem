package com.example.lib.controller;

import com.example.lib.*;
import com.example.lib.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static com.example.lib.HelloWorldApplication.isUserAdminOrOwner;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/sitz")
public class SitzController {

    @Autowired
    SitzRepository sitzRepository;

    @Autowired
    KinosaalRepository kinosaalRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @RequestMapping(value = "/vorstellung/{vorstellung_id}", produces = "application/json")
    public ResponseEntity<Object> getAllSitzeBelegt(@PathVariable(value = "vorstellung_id") int vorstellung_id) {
        Optional<Vorstellung> oV = vorstellungRepository.findById(vorstellung_id);
        if (oV.isPresent()) {
            Vorstellung vorstellung = oV.get();
            Kinosaal kinosaal = vorstellung.getSaal();
            Ticket[] tickets = ticketRepository.findByVorstellung(vorstellung);
            Sitz[] sitze = sitzRepository.findByKinosaal(kinosaal);
            VorstellungsSitz[] vSitze = new VorstellungsSitz[sitze.length];
            int index = 0;
            boolean isBesetzt;
            for (Sitz sitz : sitze) {
                isBesetzt = false;
                for (Ticket ticket : tickets) {
                    if (ticket.getIstValide() && ticket.getSitz() == sitz) {
                        isBesetzt = true;
                    }
                }
                vSitze[index] = new VorstellungsSitz(sitz, isBesetzt);
                index++;
            }
            return new ResponseEntity<>(vSitze, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Vorstellung nicht gefunden", HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/insert/Sitz/{kinosaal_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setSitzImKinosaal(@RequestBody() Sitz sitz,
                                                    @PathVariable(value = "kinosaal_id") long kinosaal_id, Principal principal) {

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Keine Benutzer gefunden", HttpStatus.FORBIDDEN);
        Benutzer benutzer = optionalBenutzer.get();
        if (!isUserAdminOrOwner(benutzer))
            return new ResponseEntity<>("Keine Admin Berechtigung", HttpStatus.FORBIDDEN);

        Optional<Kinosaal> optionalKinosaal = kinosaalRepository.findById((int) kinosaal_id);

        if (optionalKinosaal.isPresent()) {
            Kinosaal kinosaal = optionalKinosaal.get();
            // Prüfen ob der Sitz in das Kinosaal passt, falls nicht Kinosaal "vergrößern"
            if (kinosaal.getReihe() < sitz.getReihe()) kinosaal.setReihe(sitz.getReihe());
            if (kinosaal.getSpalte() < sitz.getSpalte()) kinosaal.setSpalte(sitz.getSpalte());
            kinosaalRepository.save(kinosaal);
            sitz.setKinosaal(kinosaal);
            sitzRepository.save(sitz);
            return new ResponseEntity<>(kinosaal, HttpStatus.OK);
        } else {
            // falls kein Kinosaal da wird erstellt
            Kinosaal kinosaal = new Kinosaal(sitz.getReihe(), sitz.getSpalte());
            kinosaalRepository.save(kinosaal);
            sitz.setKinosaal(kinosaal);
            return new ResponseEntity<>(kinosaal, HttpStatus.OK);
        }
    }
}
