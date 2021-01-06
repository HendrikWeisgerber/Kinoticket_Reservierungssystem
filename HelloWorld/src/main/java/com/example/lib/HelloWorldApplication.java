package com.example.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import java.util.*;

import com.example.lib.Film;
import com.example.lib.Kinosaal;
import com.example.lib.Sitz;
import com.example.lib.Repositories.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*")
@SpringBootApplication
@RestController
public class HelloWorldApplication {
    @Autowired
    private SitzRepository sitzRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    WarenkorbRepository warenkorbRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @Autowired
    BestellungRepository bestellungRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    KinosaalRepository kinosaalRepository;

    @RequestMapping(value = "/reset", produces = "application/json")
    public ResponseEntity<Object> home() throws ParseException {
        ticketRepository.deleteAll();

        sitzRepository.deleteAll();
        warenkorbRepository.deleteAll();
        vorstellungRepository.deleteAll();
        bestellungRepository.deleteAll();
        filmRepository.deleteAll();
        kinosaalRepository.deleteAll();
        benutzerRepository.deleteAll();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = sdf.parse("2020-12-26 15:30:00.000");
        Date date2 = sdf.parse("2020-12-26 20:30:00.000");
        Date date3 = sdf.parse("2020-12-26 21:30:00.000");

        Vorstellung testVor = new Vorstellung(date1, new BigDecimal(8), true);
        Vorstellung testVor2 = new Vorstellung(date2, new BigDecimal(9), true);
        Vorstellung testVor3 = new Vorstellung(date3, new BigDecimal(9), true);
        Film filmT = new Film("Star Wars", "Bild", "Das ist ein neuer Film", 9, 140, 12, true, "Sci-Fi");
        Film filmT2 = new Film("Harry Potter", "Bild", "Das ist ein noch neuerer Film", 8, 150, 12, true, "Fantasy");
        Kinosaal saalT = new Kinosaal(50, 5, 10);

        filmRepository.save(filmT);
        filmRepository.save(filmT2);
        kinosaalRepository.save(saalT);
        for (int i = 1; i < 3; i++) {
            for (int k = 1; k < 3; k++) {
                Sitz sitz = new Sitz(i, k, false, new BigDecimal(1));
                sitz.setKinosaalId(saalT.getId());
                sitzRepository.save(sitz);
            }
        }
        testVor.setFilmId(filmT.getId());
        testVor.setSaal(saalT);
        testVor2.setFilmId(filmT.getId());
        testVor2.setSaal(saalT);
        testVor3.setFilmId(filmT2.getId());
        testVor3.setSaal(saalT);
        vorstellungRepository.save(testVor);
        vorstellungRepository.save(testVor2);
        vorstellungRepository.save(testVor3);


        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaalId(saal.getId());
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        }

        Ticket testT = new Ticket();
        Benutzer testBenutzer = new Benutzer();
        Warenkorb testWarenkorb = new Warenkorb();
        testBenutzer.setVorname("Max");
        testBenutzer.setNachname("Mustermann");
        testBenutzer.setUsername("Mustermann_Max");
        testBenutzer.setAlter(25);
        testBenutzer.setEmail("max.mustermann@gmail.com");
        testBenutzer.setPasswortHash("KFIWN");
        testBenutzer.setWarenkorb(testWarenkorb);
        testWarenkorb.setBenutzer(testBenutzer);
        Bestellung testBestellung = new Bestellung();
        ticketRepository.save(testT);
        bestellungRepository.save(testBestellung);
        benutzerRepository.save(testBenutzer);


        testBestellung.setBenutzer(testBenutzer);
        testT.setVorstellung(testVor);
        testT.setSitz(testVor.getSaal().getMeineSitze().get(0));
        testT.setGast(testBenutzer);
        testT.setKaeufer(testBenutzer);
        testT.setBezahlt(true);
        testT.setIstValide(false);
        testT.setBestellung(testBestellung);
        testT.setBezahlt(false);

        ticketRepository.deleteById(testT.getId());
        ticketRepository.save(testT);

        return new ResponseEntity<>("Die Datenbank wurde erfolgreich resettet!", HttpStatus.OK);
    }

    @RequestMapping(value = "/crud/ticket/{vorstellung_id}", produces = "application/json")
    public ResponseEntity<Object> getAllTickets(@PathVariable(value = "vorstellung_id") long vorstellung_id, Pageable pageable) {

        Ticket testT = new Ticket();
        Vorstellung testV = new Vorstellung();
        Sitz testSitz = new Sitz(3, 5, true, new BigDecimal(2));
        Benutzer testBenutzer = new Benutzer();
        vorstellungRepository.save(testV);
        sitzRepository.save(testSitz);
        benutzerRepository.save(testBenutzer);

        testT.setSitz(testSitz);
        testT.setVorstellung(testV);
        testT.setKaeufer(testBenutzer);
        testT.setGast(testBenutzer);
        testT.setBezahlt(true);
        testT.setIstValide(false);

        ticketRepository.save(testT);

        Ticket testT2 = new Ticket();
        testT2.setSitz(testSitz);
        testT2.setVorstellung(testV);
        testT2.setKaeufer(testBenutzer);
        testT2.setGast(testBenutzer);
        testT2.setBezahlt(false);
        testT2.setIstValide(false);

        ticketRepository.save(testT2);


        return new ResponseEntity<>(ticketRepository.findByVorstellungId((int) vorstellung_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/bestellung/nutzer/{nutzer_id}", produces = "application/json")
    public ResponseEntity<Object> getAllTicketsInBestellung(@PathVariable(value = "nutzer_id") long nutzer_id, Pageable pageable) {

        Optional<Benutzer> oB = benutzerRepository.findById((Integer) (int) nutzer_id);
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

    @RequestMapping(value = "/warenkorb/nutzer/{nutzer_id}", produces = "application/json")
    public ResponseEntity<Object> getAllTicketsInWarenkorb(@PathVariable(value = "nutzer_id") long nutzer_id, Pageable pageable) {

		/*
		Ticket testT = new Ticket();
		Vorstellung testV = new Vorstellung();
		Sitz testSitz = new Sitz(1,3,5,true,new BigDecimal(2));
		Benutzer testBenutzer = new Benutzer();
		Warenkorb testWarenkorb = new Warenkorb();
		testWarenkorb.setBenutzer(testBenutzer);

		warenkorbRepository.save(testWarenkorb);
		vorstellungRepository.save(testV);
		sitzRepository.save(testSitz);
		benutzerRepository.save(testBenutzer);

		testT.setSitz(testSitz);
		testT.setVorstellung(testV);

		testT.setGast(testBenutzer);
		testT.setKaeufer(testBenutzer);
		testT.setBezahlt(true);
		testT.setIstValide(false);
		testT.setWarenkorb(testWarenkorb);
		testT.setBezahlt(false);

		ticketRepository.save(testT);
		int b_id = testBenutzer.getId();
		*/

        Optional<Benutzer> oB = benutzerRepository.findById((Integer) (int) nutzer_id);
        Benutzer b;
        if (!oB.isEmpty()) {
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

    // TODO implement MappingMethods
    @RequestMapping(value = "/vorstellung", produces = "application/json")
    public ResponseEntity<Object> getVorstellung(Pageable pageable) {

        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaalId(saal.getId());
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        }
        /*Vorstellung testVor = new Vorstellung();
        Film filmT = new Film();
        filmRepository.save(filmT);

        testVor.setFilm(filmT);
        vorstellungRepository.save(testVor);*/

        return new ResponseEntity<>(vorstellungRepository.findAll(), HttpStatus.OK);
        //return new ResponseEntity<>(vorstellungRepository.findByFilmId((int)film_id),HttpStatus.OK);
    }

    @RequestMapping(value = "/vorstellung/film/{film_id}", produces = "application/json")
    public ResponseEntity<Object> getVorstellungByFilm(@PathVariable(value = "film_id") int film_id, Pageable pageable) {

        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaalId(saal.getId());
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        }

        return new ResponseEntity<>(vorstellungRepository.findByFilmId(film_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/kinosaal/vorstellung/{vorstellung_id}", produces = "application/json")
    public ResponseEntity<Object> getSaalByVorstellung(@PathVariable(value = "vorstellung_id") int vorstellung_id, Pageable pageable) {

        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaalId(saal.getId());
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        }

        return new ResponseEntity<>(vorstellungRepository.findById(vorstellung_id).get().getSaal(), HttpStatus.OK);
    }

    @RequestMapping(value = "/crud/benutzer/all", produces = "application/json")
    public ResponseEntity<Object> getAllBenutzer() {

        Warenkorb testW = new Warenkorb();
        Benutzer testB = new Benutzer();
        testW.setBenutzer(testB);
        warenkorbRepository.save(testW);
        testB.setVorname("Max");
        testB.setNachname("Mustermann");
        testB.setUsername("Mustermann_Max");
        testB.setAlter(25);
        testB.setEmail("max.mustermann@gmail.com");
        testB.setPasswortHash("KFIWN");
        testB.setWarenkorb(new Warenkorb());

        //testB.setNewsletter(false);
        //testB.derWunschlisteHinzufuegen(new Film("Star Wars", "hier ist das Bild", "das passiert", 10, 200, 12, true, "Sci-Fi"));

        benutzerRepository.save(testB);

        return new ResponseEntity<>(benutzerRepository.findAll(), HttpStatus.OK);

    }

    @RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/benutzer/{benutzer_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setTicketOhneGast(@PathVariable(value = "sitz_id") long sitz_id,
                                                    @PathVariable(value = "vorstellung_id") long vorstellung_id,
                                                    @PathVariable(value = "benutzer_id") long kaeufer_id) {

        Object o = makeTicket(sitz_id, vorstellung_id, kaeufer_id).getBody();
        if (o instanceof Ticket) {
            Ticket ticket = (Ticket) o;
            ticketRepository.save(ticket);
            return new ResponseEntity<>("Ticket wurde gespeichert, Kaeufer entspricht dem Gast", HttpStatus.OK);
        }

        return new ResponseEntity<>(o, HttpStatus.OK);

    }

    @RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/benutzer/{benutzer_id}/gast/{gast_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setTicketMitGast(@PathVariable(value = "sitz_id") long sitz_id,
                                                   @PathVariable(value = "vorstellung_id") long vorstellung_id,
                                                   @PathVariable(value = "benutzer_id") long kaeufer_id,
                                                   @PathVariable(value = "gast_id") long gast_id) {
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
            return new ResponseEntity<>("Ticket wurde gespeichert, der Besteller entspricht dem Gast", HttpStatus.OK);
        }

        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @RequestMapping(value = "warenkorb/benutzer/{benutzer_id}/ticket/{ticket_id}", produces = "appliation/json")
    public ResponseEntity<Object> saveTicketInWarenkorb(@PathVariable(value = "benutzer_id") long benutzer_id,
                                                        @PathVariable(value = "ticket_id") long ticket_id) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findById((int) benutzer_id);
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
                t.setWarenkorb(benutzer.getWarenkorb());
                ticketRepository.save(t);
                return new ResponseEntity<Object>(t, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }

    @RequestMapping(value = "/ticket/bestellung/benutzer/{benutzer_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setBestellung(@RequestBody(required = true) int[] ticketIds,
                                                @PathVariable(value = "benutzer_id") long benutzer_id) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findById((int) benutzer_id);
        if (optionalBenutzer.isPresent()) {
            Benutzer b = optionalBenutzer.get();
            if (b.getWarenkorb() == null) {
                Warenkorb w = new Warenkorb();
                b.setWarenkorb(w);
                benutzerRepository.save(b);
                warenkorbRepository.save(w);
            }

            /*for (Ticket t: tickets) {
                Optional<Ticket> optionalT = ticketRepository.findById(t.getId());
                if (optionalT.isPresent()) {
                    Ticket ticket = optionalT.get();
                    ticket.setBezahlt(false);
                }
            }*/
            Bestellung bestellung = b.getWarenkorb().reservieren(ticketIds, ticketRepository);
            //TODO Sicherheitslücke wenn die Methode so bleibt. Der Benutzer könnte jegliche Tickets mit allen möglichen Daten selbst speichern wie er will
            bestellung.setBenutzer(b);
            bestellungRepository.save(bestellung);

            return new ResponseEntity<Object>(bestellung, HttpStatus.OK);
        }
        return new ResponseEntity<Object>("OO", HttpStatus.OK);
    }

    public static void main(String[] args) {
        //SpringApplication.run(HelloWorldApplication.class, args);
        SpringApplication app = new SpringApplication(HelloWorldApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);
    }

    private ResponseEntity<Object> makeTicket(@PathVariable(value = "sitz_id") long sitz_id,
                                              @PathVariable(value = "vorstellung_id") long vorstellung_id,
                                              @PathVariable(value = "benutzer_id") long kaeufer_id) {
        Ticket ticket = new Ticket();
        Sitz sitz = new Sitz();
        Vorstellung vorstellung = new Vorstellung();
        Benutzer kaeufer = new Benutzer();

        // Prüfe ob das Ticket bereits existiert
        Ticket[] ticketsByVorstellung = ticketRepository.findByVorstellungId((int) vorstellung_id);
        if (ticketsByVorstellung != null) {
            for (Ticket t : ticketsByVorstellung) {
                if (t.getSitz().getId() == sitz_id) {
                    return new ResponseEntity<>("Das Ticket ist leider nicht mehr verfügbar", HttpStatus.OK);
                }
            }
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
}
