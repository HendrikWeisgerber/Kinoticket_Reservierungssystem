package com.example.lib;

import org.hibernate.sql.Delete;
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

	    sitzRepository.deleteAll();
	    ticketRepository.deleteAll();
	    warenkorbRepository.deleteAll();
	    vorstellungRepository.deleteAll();
	    bestellungRepository.deleteAll();
	    filmRepository.deleteAll();
	    kinosaalRepository.deleteAll();
        benutzerRepository.deleteAll();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = sdf.parse("2020-12-26 20:30:00.000");

        Vorstellung testVor = new Vorstellung(date, new BigDecimal(9), true);
		Vorstellung testVor2 = new Vorstellung();
		Vorstellung testVor3 = new Vorstellung();
        Film filmT = new Film("Star Wars", "Bild", "Das ist ein neuer Film", 9, 140, 12, true, "Sci-Fi");
		Film filmT2 = new Film("Harry Potter", "Bild", "Das ist ein noch neuerer Film", 8, 150, 12, true, "Fantasy");
		Kinosaal saalT = new Kinosaal(50,5,10);

        filmRepository.save(filmT);
        filmRepository.save(filmT2);
        kinosaalRepository.save(saalT);
        testVor.setFilmId(filmT.getId());
        testVor.setSaal(saalT);
		testVor2.setFilmId(filmT.getId());
		testVor2.setGrundpreis(new BigDecimal(11));
		testVor3.setFilmId(filmT2.getId());
		testVor3.setGrundpreis(new BigDecimal(8));
        vorstellungRepository.save(testVor);
		vorstellungRepository.save(testVor2);
		vorstellungRepository.save(testVor3);

		return new ResponseEntity<>(sitzRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/crud/ticket/{vorstellung_id}", produces = "application/json")
	public ResponseEntity<Object> getAllTickets(@PathVariable(value = "vorstellung_id")long vorstellung_id, Pageable pageable){

		Ticket testT = new Ticket();
		Vorstellung testV = new Vorstellung();
		Sitz testSitz = new Sitz(1,3,5,true,new BigDecimal(2));
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


		return new ResponseEntity<>(ticketRepository.findByVorstellungId((int)vorstellung_id),HttpStatus.OK);
	}
	@RequestMapping(value = "/bestellung/nutzer/{nutzer_id}", produces = "application/json")
	public ResponseEntity<Object> getAllTicketsInBestellung(@PathVariable(value = "nutzer_id")long nutzer_id, Pageable pageable){
		/*
		Ticket testT = new Ticket();
		Vorstellung testV = new Vorstellung();
		Sitz testSitz = new Sitz(1,3,5,true,new BigDecimal(2));
		Benutzer testBenutzer = new Benutzer();
		Bestellung testBestellung = new Bestellung();
		ticketRepository.save(testT);
		bestellungRepository.save(testBestellung);
		vorstellungRepository.save(testV);
		sitzRepository.save(testSitz);
		benutzerRepository.save(testBenutzer);


		testBestellung.setBenutzer(testBenutzer);


		testT.setSitz(testSitz);
		testT.setVorstellung(testV);

		testT.setGast(testBenutzer);
		testT.setKaeufer(testBenutzer);
		testT.setBezahlt(true);
		testT.setIstValide(false);
		testT.setBestellung(testBestellung);
		testT.setBezahlt(false);

		ticketRepository.deleteById(testT.getId());
		ticketRepository.save(testT);

		int b_id = testBenutzer.getId();
		*/
		Optional<Benutzer> oB = benutzerRepository.findById((Integer)(int)nutzer_id);
		Benutzer b;
		if(!oB.isEmpty()){
			b = oB.get();
		}else{
			b = null;
		}
		Bestellung[] bestellung;
		if(b != null){
			bestellung = bestellungRepository.findByBenutzer(b);
		}else{
			bestellung = new Bestellung[0];
		}
		Ticket[] t;
		if(bestellung.length>0){
			t =  ticketRepository.findByBestellung(bestellung[0]);
		}else{
			t = new Ticket[0];
		}

		return new ResponseEntity<>(t,HttpStatus.OK);
	}
	@RequestMapping(value = "/warenkorb/nutzer/{nutzer_id}", produces = "application/json")
	public ResponseEntity<Object> getAllTicketsInWarenkorb(@PathVariable(value = "nutzer_id")long nutzer_id, Pageable pageable){

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

		Optional<Benutzer> oB = benutzerRepository.findById((Integer)(int)nutzer_id);
		Benutzer b;
		if(!oB.isEmpty()){
			b = oB.get();
		}else{
			b = null;
		}
		Warenkorb[] w;
		if(b != null){
			w = warenkorbRepository.findByBenutzer(b);
		}else{
			w = new Warenkorb[0];
		}
		Ticket[] t;
		if(w.length>0){
			t =  ticketRepository.findByWarenkorb(w[0]);
		}else{
			t = new Ticket[0];
		}

		return new ResponseEntity<>(t,HttpStatus.OK);

	}

	// TODO implement MappingMethods
    @RequestMapping(value = "/vorstellung", produces = "application/json")
    public ResponseEntity<Object> getVorstellung(Pageable pageable){

        /*Vorstellung testVor = new Vorstellung();
        Film filmT = new Film();
        filmRepository.save(filmT);

        testVor.setFilm(filmT);
        vorstellungRepository.save(testVor);*/

        return new ResponseEntity<>(vorstellungRepository.findAll(),HttpStatus.OK);
        //return new ResponseEntity<>(vorstellungRepository.findByFilmId((int)film_id),HttpStatus.OK);
    }

    @RequestMapping(value = "/vorstellung/film/{film_id}", produces = "application/json")
    public ResponseEntity<Object> getVorstellungByFilm(@PathVariable(value = "film_id")long film_id, Pageable pageable){

        /*Vorstellung testVor = new Vorstellung();
        Film filmT = new Film();
        filmRepository.save(filmT);

        testVor.setFilm(filmT);
        vorstellungRepository.save(testVor);*/

        return new ResponseEntity<>(vorstellungRepository.findByFilmId((int)film_id),HttpStatus.OK);
    }

    @RequestMapping(value = "/kinosaal/vorstellung/{vorstellung_id}", produces = "application/json")
    public ResponseEntity<Object> getSaalByVorstellung(@PathVariable(value = "vorstellung_id")int vorstellung_id, Pageable pageable){

        /*ArrayList<Sitz> sitze = new ArrayList<Sitz>();
        Sitz testSitz = new Sitz(1,1,1,true,new BigDecimal(2));
        Sitz testSitz2 = new Sitz(2,1,2,true,new BigDecimal(2));
        Sitz testSitz3 = new Sitz(3,1,3,true,new BigDecimal(2));

        sitzRepository.save(testSitz);
        sitzRepository.save(testSitz2);
        sitzRepository.save(testSitz3);
        sitze.add(testSitz);
        sitze.add(testSitz2);
        sitze.add(testSitz3);
        Kinosaal testSaal = new Kinosaal();
        testSitz.setMeinKinosaal(testSaal);
        testSitz2.setMeinKinosaal(testSaal);
        testSitz3.setMeinKinosaal(testSaal);
        testSaal.setMeineSitze(sitze);

        kinosaalRepository.save(testSaal);
        Vorstellung testVor = new Vorstellung();
        Film filmT = new Film();
        filmRepository.save(filmT);

        testVor.setFilm(filmT);
        testVor.setSaal(testSaal);
        vorstellungRepository.save(testVor);*/

        return new ResponseEntity<>(vorstellungRepository.findById(vorstellung_id).get().getSaal(),HttpStatus.OK);
    }

	@RequestMapping(value = "/crud/benutzer/all", produces = "application/json")
	public ResponseEntity<Object> getAllBenutzer(){

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

		return new ResponseEntity<>(benutzerRepository.findAll(),HttpStatus.OK);

	}

	public static void main(String[] args) {
		//SpringApplication.run(HelloWorldApplication.class, args);
		SpringApplication app =new SpringApplication(HelloWorldApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		app.run(args);

	}
}
