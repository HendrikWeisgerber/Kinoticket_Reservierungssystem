package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Repositories.BenutzerRepository;
import com.example.lib.Repositories.SitzRepository;
import com.example.lib.Repositories.TicketRepository;
import com.example.lib.Repositories.VorstellungRepository;
import com.example.lib.Sitz;
import com.example.lib.Ticket;
import com.example.lib.Vorstellung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.Semaphore;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/ticket")
public class TicketController {

    private static Semaphore mutex = new Semaphore(1, true);
    ;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    SitzRepository sitzRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @RequestMapping(value = "/sitz/{sitz_id}/vorstellung/{vorstellung_id}/benutzer/{benutzer_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setTicketOhneGast(@PathVariable(value = "sitz_id") long sitz_id,
                                                    @PathVariable(value = "vorstellung_id") long vorstellung_id,
                                                    @PathVariable(value = "benutzer_id") long kaeufer_id) {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object o = makeTicket(sitz_id, vorstellung_id, kaeufer_id).getBody();
        if (o instanceof Ticket) {
            Ticket ticket = (Ticket) o;
            ticketRepository.save(ticket);
            mutex.release();
            return new ResponseEntity<>("Ticket wurde gespeichert, Kaeufer entspricht dem Gast", HttpStatus.OK);
        }
        mutex.release();
        return new ResponseEntity<>(o, HttpStatus.OK);

    }

    @RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/benutzer/{benutzer_id}/gast/{gast_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setTicketMitGast(@PathVariable(value = "sitz_id") long sitz_id,
                                                   @PathVariable(value = "vorstellung_id") long vorstellung_id,
                                                   @PathVariable(value = "benutzer_id") long kaeufer_id,
                                                   @PathVariable(value = "gast_id") long gast_id) {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Benutzer gast = new Benutzer();

        Object o = makeTicket(sitz_id, vorstellung_id, kaeufer_id).getBody();
        if (o instanceof Ticket) {
            Ticket ticket = (Ticket) o;
            Optional<Benutzer> gastOptional = benutzerRepository.findById((int) gast_id);
            if (gastOptional.isPresent()) {
                gast = gastOptional.get();
                ticket.setGast(gast);
            } else {
                System.out.println("Kein Gast gefunden");
                return new ResponseEntity<>("Kein Gast gefunden", HttpStatus.OK);
            }
            ticketRepository.save(ticket);
            mutex.release();
            return new ResponseEntity<>("Ticket wurde gespeichert, der Besteller entspricht dem Gast", HttpStatus.OK);
        }
        mutex.release();
        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    // Diese Methode darf nur in einem Semaphor aufgerufen werden!!!
    private ResponseEntity<Object> makeTicket(long sitz_id, long vorstellung_id, long kaeufer_id) {
        Ticket ticket = new Ticket();
        Sitz sitz = new Sitz();
        Vorstellung vorstellung = new Vorstellung();
        Benutzer kaeufer = new Benutzer();

        // Prüfe ob das Ticket bereits existiert
        Ticket[] ticketsByVorstellung = ticketRepository.findByVorstellungId((int) vorstellung_id);
        if (ticketExistiertBereits(sitz_id, vorstellung_id)) {
            return new ResponseEntity<>("Das Ticket ist leider nicht mehr verfügbar", HttpStatus.OK);
        }

        Optional<Sitz> sitzOptional = sitzRepository.findById((int) sitz_id);
        if (sitzOptional.isPresent()) {
            sitz = sitzOptional.get();
            ticket.setSitz(sitz);

        } else {
            /*sitz.setSpalte(5);
            sitz.setReihe(5);
            sitz.setPreisschluessel(new BigDecimal(5));
            sitz.setBarriereFrei(true);
            Kinosaal kinosaal = new Kinosaal();
            kinosaal.setAnzahlSitze(25);
            kinosaal.setReihe(5);
            kinosaal.setSpalte(5);
            kinosaalRepository.save(kinosaal);
            sitz.setKinosaalId(kinosaal.getId());
            sitzRepository.save(sitz);
            ticket.setSitz(sitz);*/
            System.out.println("Kein Sitz gefunden");
            return new ResponseEntity<>("Kein Sitz gefunden", HttpStatus.OK);
        }

        Optional<Vorstellung> vorstellungOptional = vorstellungRepository.findById((int) vorstellung_id);
        if (vorstellungOptional.isPresent()) {
            vorstellung = vorstellungOptional.get();
            ticket.setVorstellung(vorstellung);
        } else {
            System.out.println("Keine Vorstellung gefunden");
            return new ResponseEntity<>("Keine Vorstellung gefunden", HttpStatus.OK);
        }

        Optional<Benutzer> kaeuferOptional = benutzerRepository.findById((int) kaeufer_id);
        if (kaeuferOptional.isPresent()) {
            kaeufer = kaeuferOptional.get();
            ticket.setKaeufer(kaeufer);
        } else {
            System.out.println("Keine Kaeufer gefunden");
            return new ResponseEntity<>("Kein Kaeufer gefunden", HttpStatus.OK);
        }
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    private boolean ticketExistiertBereits(long sitz_id, long vorstellung_id) {
        Optional<Sitz> oSitz = sitzRepository.findById((int) sitz_id);
        Optional<Vorstellung> oVorstellung = vorstellungRepository.findById((int) vorstellung_id);

        Sitz sitz;
        Vorstellung vorstellung;

        if (oSitz.isEmpty()) return false;
        if (oVorstellung.isEmpty()) return false;

        vorstellung = oVorstellung.get();
        sitz = oSitz.get();

        Ticket[] ticketsMitSitz, ticketsMitVorstellung;
        ticketsMitSitz = ticketRepository.findBySitz(sitz);
        ticketsMitVorstellung = ticketRepository.findByVorstellung(vorstellung);

        if (ticketsMitSitz.length == 0 || ticketsMitVorstellung.length == 0) {
            return false;
        }

        for (Ticket ticketMitSitz : ticketsMitSitz) {
            for (Ticket ticketMitVorstellung : ticketsMitVorstellung) {
                if (ticketMitSitz.getVorstellung() == ticketMitVorstellung.getVorstellung()
                        && ticketMitSitz.getSitz() == ticketMitVorstellung.getSitz()
                        && ticketMitSitz.getIstValide()
                        && ticketMitVorstellung.getIstValide()) {
                    return true;
                }
            }
        }
        return false;

    }
}
