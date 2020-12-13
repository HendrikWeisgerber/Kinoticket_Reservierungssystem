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
import java.util.*;

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


	@RequestMapping(value = "/reset", produces = "application/json")
	public ResponseEntity<Object> home(){

	    sitzRepository.deleteAll();
	    ticketRepository.deleteAll();
	    //benutzerRepository.deleteAll();
	    warenkorbRepository.deleteAll();
	    vorstellungRepository.deleteAll();
	    bestellungRepository.deleteAll();
	    filmRepository.deleteAll();
	    kinosaalRepository.deleteAll();
        benutzerRepository.deleteAll();

        Vorstellung testVor = new Vorstellung();
        Film filmT = new Film("Star Wars", "Bild", "Das ist ein neuer Film", 9, 140, 12, true, "Sci-Fi");

        filmRepository.save(filmT);
        testVor.setFilmId(filmT.getId());
        vorstellungRepository.save(testVor);

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


        return new ResponseEntity<>(ticketRepository.findByVorstellungId((int) vorstellung_id), HttpStatus.OK);
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

	@RequestMapping(value= "/film/all", produces ="application/json")
	public ResponseEntity<Object> getAllFilms(){
		/*Film philosopherStone = new Film();
		Film champerOfSecrets = new Film();

		philosopherStone.setAktiv(true);
		philosopherStone.setBeschreibung("What a fantastic start of what is yet to come");
		philosopherStone.setBild("A Picture of Stone");
		philosopherStone.setBewertung(10);
		philosopherStone.setId(1);
		philosopherStone.setLaenge(2);
		philosopherStone.setMindestAlter(6);
		philosopherStone.setName("Henry Otter and the Knoledge Stone");

		champerOfSecrets.setAktiv(true);
		champerOfSecrets.setBeschreibung("It did do the fantastic start justice");
		champerOfSecrets.setBild("A Picture of a Secret :D");
		champerOfSecrets.setBewertung(10);
		champerOfSecrets.setId(2);
		champerOfSecrets.setLaenge(2);
		champerOfSecrets.setMindestAlter(12);
		champerOfSecrets.setName("Henry Otter and the Dungeon of Fakenews");

		filmRepository.save(philosopherStone);
		filmRepository.save(champerOfSecrets);*/


		return new ResponseEntity<>(filmRepository.findAll(), HttpStatus.OK);
	}


	@RequestMapping(value = "/film/{film_id}", produces = "application/json")
	public ResponseEntity<Object> getFilmbyID(@PathVariable(value = "film_id")long film_id, Pageable pageable){

		Film philosopherStone = new Film();
		Film champerOfSecrets = new Film();

		philosopherStone.setAktiv(true);
		philosopherStone.setBeschreibung("What a fantastic start of what is yet to come");
		philosopherStone.setBild("A Picture of Stone");
		philosopherStone.setBewertung(10);
		philosopherStone.setId(1);
		philosopherStone.setLaenge(2);
		philosopherStone.setMindestAlter(6);
		philosopherStone.setName("Henry Otter and the Knoledge Stone");

		champerOfSecrets.setAktiv(true);
		champerOfSecrets.setBeschreibung("It did do the fantastic start justice");
		champerOfSecrets.setBild("A Picture of a Secret :D");
		champerOfSecrets.setBewertung(10);
		champerOfSecrets.setId(2);
		champerOfSecrets.setLaenge(2);
		champerOfSecrets.setMindestAlter(12);
		champerOfSecrets.setName("Henry Otter and the Dungeon of Fakenews");

		filmRepository.save(philosopherStone);
		filmRepository.save(champerOfSecrets);


		return new ResponseEntity<>(filmRepository.findById((int)film_id), HttpStatus.OK);
	}

	/*
	@RequestMapping(value = "/vorstellung/film/{film_id}", produces = "application/json")Niklas!!
	@RequestMapping(value = "/sitz/vorstellung/{vorstellung_id}", produces = "application/json")Niklas!!
	@RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/nutzer/{nutzer_id}", produces = "application/json")
	@RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/nutzer/{nutzer_id}/gast/{gast_id)", produces = "application/json")
	@RequestMapping(value = "/bestellung/nutzer/{nutzer_id}", produces = "application/json")
	*/
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

        ArrayList<Sitz> sitze = new ArrayList<Sitz>();
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

        testVor.setFilmId(filmT.getId());
        testVor.setSaal(testSaal);
        vorstellungRepository.save(testVor);

        return new ResponseEntity<>(vorstellungRepository.findById(vorstellung_id).get().getSaal(),HttpStatus.OK);
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

		return new ResponseEntity<>(benutzerRepository.findAll(),HttpStatus.OK);

	}

    @RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/benutzer/{benutzer_id}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setTicket(@PathVariable(value = "sitz_id") long sitz_id,
                                            @PathVariable(value = "vorstellung_id") long vorstellung_id,
                                            @PathVariable(value = "benutzer_id") long kaeufer_id) {

        Ticket ticket = new Ticket();
        Sitz sitz = new Sitz();
        Vorstellung vorstellung = new Vorstellung();
        Benutzer kaeufer = new Benutzer();

        Optional<Sitz> sitzOptional = sitzRepository.findById((int) sitz_id);
        if (sitzOptional.isPresent()) {
            sitz = sitzOptional.get();
            ticket.setSitz(sitz);

        } else {
            sitz.setSpalte(5);
            sitz.setReihe(5);
            sitz.setPreisschluessel(new BigDecimal(5));
            sitz.setBarriereFrei(true);
            Kinosaal kinosaal = new Kinosaal();
            kinosaal.setAnzahlSitze(25);
            kinosaal.setReihe(5);
            kinosaal.setSpalte(5);
            kinosaalRepository.save(kinosaal);
            sitz.setMeinKinosaal(kinosaal);
            sitzRepository.save(sitz);
            ticket.setSitz(sitz);
            System.out.println("Kein Sitz gefunden");
        }

        Optional<Vorstellung> vorstellungOptional = vorstellungRepository.findById((int) vorstellung_id);
        if (vorstellungOptional.isPresent()) {
            vorstellung = vorstellungOptional.get();
            ticket.setVorstellung(vorstellung);
        } else {
            System.out.println("Keine Vorstellung gefunden");
        }

        Optional<Benutzer> kaeuferOptional = benutzerRepository.findById((int) kaeufer_id);
        if (kaeuferOptional.isPresent()) {
            kaeufer = kaeuferOptional.get();
            ticket.setKaeufer(kaeufer);
        } else {
            System.out.println("Keine Kaeufer gefunden");
        }


        ticketRepository.save(ticket);

        return new ResponseEntity<>("Ticket wurde gespeichert, der Besteller entspricht dem Gast", HttpStatus.OK);
    }

	@RequestMapping(value = "/ticket/sitz/{sitz_id}/vorstellung/{vorstellung_id}/benutzer/{benutzer_id}/gast/{gast_id)", produces = "application/json", method = POST)
	public ResponseEntity<Object> setTicket(@PathVariable(value = "sitz_id") long sitz_id,
											@PathVariable(value = "vorstellung_id") long vorstellung_id,
											@PathVariable(value = "benutzer_id") long kaeufer_id,
											@PathVariable(value = "gast_id") long gast_id) {

        Ticket ticket = new Ticket();
        Sitz sitz = new Sitz();
		Vorstellung vorstellung = new Vorstellung();
		Benutzer kaeufer = new Benutzer();
		Benutzer gast = new Benutzer();

		Optional<Sitz> sitzOptional = sitzRepository.findById((int) sitz_id);
		if (sitzOptional.isPresent()) {
			sitz = sitzOptional.get();
            ticket.setSitz(sitz);
        } else {
			System.out.println("Kein Sitz gefunden");
		}

		Optional<Vorstellung> vorstellungOptional = vorstellungRepository.findById((int) vorstellung_id);
		if (vorstellungOptional.isPresent()) {
			vorstellung = vorstellungOptional.get();
            ticket.setVorstellung(vorstellung);
        } else {
			System.out.println("Keine Vorstellung gefunden");
		}

		Optional<Benutzer> kaeuferOptional = benutzerRepository.findById((int) kaeufer_id);
		if (kaeuferOptional.isPresent()) {
			kaeufer = kaeuferOptional.get();
            ticket.setKaeufer(kaeufer);
        } else {
			System.out.println("Keine Kaeufer gefunden");
		}

		Optional<Benutzer> gastOptional = benutzerRepository.findById((int) gast_id);
		if (gastOptional.isPresent()) {
			gast = gastOptional.get();
            ticket.setGast(gast);
        } else {
			System.out.println("Kein Gast gefunden");
		}

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
