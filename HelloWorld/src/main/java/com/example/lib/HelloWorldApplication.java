package com.example.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

	@RequestMapping(value = "/", produces = "application/json")
	public ResponseEntity<Object> home(){
		Film starWars = new Film("Star Wars", "hier ist das Bild", "das passiert", 10, 200, 12, true, "Sci-Fi");
		Film starWars2 = new Film("Star Wars 2", "hier ist das Bild", "das passiert", 10, 200, 12, true, "Sci-Fi");
		final Map<String, Film> films = new HashMap<>();
		
		films.put("a",starWars);
		films.put("b",starWars2);
		
		// Sitz ersterSitz = new Sitz(1,3,5,true,new BigDecimal(2));
		// sitzRepository.save(ersterSitz);
		
		return new ResponseEntity<>(sitzRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/crud/ticket/all", produces = "application/json")
	public ResponseEntity<Object> getAllTickets(){

		Ticket testT = new Ticket();
		testT.setSitz(new Sitz(1,3,5,true,new BigDecimal(2)));
		testT.setVorstellung(new Vorstellung());
		testT.setKaeufer(new Benutzer());
		testT.setGast(new Benutzer());
		testT.setBezahlt(true);
		testT.setIstValide(false);

		ticketRepository.save(testT);

		return new ResponseEntity<>(ticketRepository.findAll(), HttpStatus.OK);
	}


	public static void main(String[] args) {
		//SpringApplication.run(HelloWorldApplication.class, args);
		SpringApplication app =new SpringApplication(HelloWorldApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		app.run(args);

	}
}
