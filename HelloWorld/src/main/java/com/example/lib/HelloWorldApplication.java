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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.lib.Film;
import com.example.lib.Kinosaal;
import com.example.lib.Sitz;
import com.example.lib.Repositories.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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


    @RequestMapping(value = "/", produces = "application/json")
    public ResponseEntity<Object> home() {
        Film starWars = new Film("Star Wars", "hier ist das Bild", "das passiert", 10, 200, 12, true, "Sci-Fi");
        Film starWars2 = new Film("Star Wars 2", "hier ist das Bild", "das passiert", 10, 200, 12, true, "Sci-Fi");
        final Map<String, Film> films = new HashMap<>();

        films.put("a", starWars);
        films.put("b", starWars2);

        // Sitz ersterSitz = new Sitz(1,3,5,true,new BigDecimal(2));
        // sitzRepository.save(ersterSitz);

        return new ResponseEntity<>(sitzRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/crud/ticket/{vorstellung_id}", produces = "application/json")
    public ResponseEntity<Object> getAllTickets(@PathVariable(value = "vorstellung_id") long vorstellung_id, Pageable pageable) {

        Ticket testT = new Ticket();
        Vorstellung testV = new Vorstellung();
        Sitz testSitz = new Sitz(1, 3, 5, true, new BigDecimal(2));
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

    // TODO implement MappingMethods

	/*
	@RequestMapping(value = "/film/all", produces = "application/json")
	@RequestMapping(value = "/film/{film_id}", produces = "application/json")
	@RequestMapping(value = "/vorstellung/film/{film_id}", produces = "application/json")
	@RequestMapping(value = "/sitz/vorstellung/{vorstellung_id}", produces = "application/json")
	@RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/nutzer/{nutzer_id}", produces = "application/json")
	@RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/nutzer/{nutzer_id}/gast/{gast_id)", produces = "application/json")
	@RequestMapping(value = "/warenkorb/nutzer/{nutzer_id}", produces = "application/json")
	*/

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
    public ResponseEntity<Object> setTicket(@PathVariable(value = "sitz_id") long sitz_id,
                                            @PathVariable(value = "vorstellung_id") long vorstellung_id,
                                            @PathVariable(value = "benutzer_id") long kaeufer_id) {

        Sitz sitz = new Sitz();
        Vorstellung vorstellung = new Vorstellung();
        Benutzer kaeufer = new Benutzer();

        Optional<Sitz> sitzOptional = sitzRepository.findById((int) sitz_id);
        if (sitzOptional.isPresent()) {
            sitz = sitzOptional.get();
        } else {
            System.out.println("Kein Sitz gefunden");
        }

        Optional<Vorstellung> vorstellungOptional = vorstellungRepository.findById((int) vorstellung_id);
        if (vorstellungOptional.isPresent()) {
            vorstellung = vorstellungOptional.get();
        } else {
            System.out.println("Keine Vorstellung gefunden");
        }

        Optional<Benutzer> kaeuferOptional = benutzerRepository.findById((int) kaeufer_id);
        if (kaeuferOptional.isPresent()) {
            kaeufer = kaeuferOptional.get();
        } else {
            System.out.println("Keine Kaeufer gefunden");
        }

        Ticket ticket = new Ticket();
        ticket.setSitz(sitz);
        ticket.setVorstellung(vorstellung);
        ticket.setKaeufer(kaeufer);
        ticketRepository.save(ticket);

        return new ResponseEntity<>("Ticket wurde gespeichert, der Besteller entspricht dem Gast", HttpStatus.OK);
    }

	@RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/benutzer/{benutzer_id}/gast/{gast_id)", produces = "application/json", method = POST)
	public ResponseEntity<Object> setTicket(@PathVariable(value = "sitz_id") long sitz_id,
											@PathVariable(value = "vorstellung_id") long vorstellung_id,
											@PathVariable(value = "benutzer_id") long kaeufer_id,
											@PathVariable(value = "gast_id") long gast_id) {

		Sitz sitz = new Sitz();
		Vorstellung vorstellung = new Vorstellung();
		Benutzer kaeufer = new Benutzer();
		Benutzer gast = new Benutzer();

		Optional<Sitz> sitzOptional = sitzRepository.findById((int) sitz_id);
		if (sitzOptional.isPresent()) {
			sitz = sitzOptional.get();
		} else {
			System.out.println("Kein Sitz gefunden");
		}

		Optional<Vorstellung> vorstellungOptional = vorstellungRepository.findById((int) vorstellung_id);
		if (vorstellungOptional.isPresent()) {
			vorstellung = vorstellungOptional.get();
		} else {
			System.out.println("Keine Vorstellung gefunden");
		}

		Optional<Benutzer> kaeuferOptional = benutzerRepository.findById((int) kaeufer_id);
		if (kaeuferOptional.isPresent()) {
			kaeufer = kaeuferOptional.get();
		} else {
			System.out.println("Keine Kaeufer gefunden");
		}

		Optional<Benutzer> gastOptional = benutzerRepository.findById((int) gast_id);
		if (gastOptional.isPresent()) {
			gast = gastOptional.get();
		} else {
			System.out.println("Kein Gast gefunden");
		}

		Ticket ticket = new Ticket();
		ticket.setSitz(sitz);
		ticket.setVorstellung(vorstellung);
		ticket.setKaeufer(kaeufer);
		ticket.setGast(gast);
		ticketRepository.save(ticket);

		return new ResponseEntity<>("Ticket wurde gespeichert, der Besteller entspricht NICHT dem Gast", HttpStatus.OK);
	}

    public static void main(String[] args) {
        //SpringApplication.run(HelloWorldApplication.class, args);
        SpringApplication app = new SpringApplication(HelloWorldApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);

    }
}
