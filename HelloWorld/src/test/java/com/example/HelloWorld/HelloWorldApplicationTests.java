package com.example.HelloWorld;

import com.example.lib.Bestellung;
import com.example.lib.Repositories.*;
import com.example.lib.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;

import com.example.lib.Benutzer;
import com.example.lib.Film;
import com.example.lib.Enum.Preiskategorie;
import com.example.lib.Enum.Rechte;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest(classes = { com.example.lib.HelloWorldApplication.class })
@AutoConfigureMockMvc
class HelloWorldApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FilmRepository filmrepository;

	@Autowired
	private BenutzerRepository benutzerRepository;

	@Autowired
	private WarenkorbRepository warenkorbRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private BestellungRepository bestellungRepository;

	private Benutzer benutzer;

	private String token;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		benutzer = new Benutzer();
		benutzer.setUsername("Moritz");
		benutzer.setPasswortHash("123456");
		benutzer.setPreiskategorie(Preiskategorie.ERWACHSENER);
		Assertions.assertFalse(benutzer == null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		String requestJson = asJsonString(benutzer);
		Assertions.assertFalse(requestJson.equals(""));
		this.token = this.mockMvc.perform(
				MockMvcRequestBuilders.post("/login").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Assertions.assertEquals("Bearer", this.token.split(" ")[0]);

	}

	@Test
	void contextLoads() {

	}

	//BenutzerController Tests
	@Test
	void signUp() throws Exception {
		Benutzer benutzer = new Benutzer();
		String username = "" + (int)(Math.random()*100000000);
		benutzer.setUsername(username);
		benutzer.setPasswortHash(""+(int)(Math.random()*100000));

		String requestJson = asJsonString(benutzer);
		Assertions.assertFalse(requestJson.equals(""));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/benutzer/signup").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());

		Assertions.assertTrue(benutzerRepository.findByUsername(username).isPresent());
		benutzerRepository.delete(benutzerRepository.findByUsername(username).get());
	}

	@Test
	void update() throws Exception {
		benutzer.setVorname("Alter Vorname");
		benutzer.setNachname("Alter Nachname");
		benutzer.setEmail("Alte Email");
		benutzer.setNewsletter(false);

		Benutzer updater = new Benutzer();
		updater.setVorname("Neuer Vorname");
		updater.setNachname("Neuer Nachname");
		updater.setEmail("Neue Email");
		updater.setNewsletter(true);

		String requestJson = asJsonString(updater);
		Assertions.assertFalse(requestJson.equals(""));

		mockMvc.perform(MockMvcRequestBuilders.post("/benutzer/update").contentType(APPLICATION_JSON_UTF8).content(requestJson).header("Authorization", token));

		benutzer = benutzerRepository.findByUsername("Moritz").get();

		Assertions.assertTrue(benutzer.getVorname().equals("Neuer Vorname"));
		Assertions.assertTrue(benutzer.getNachname().equals("Neuer Nachname"));
		Assertions.assertTrue(benutzer.getEmail().equals("Neue Email"));
		Assertions.assertTrue(benutzer.getNewsletter());
	}
	//Ende Benutzercontroller Tests
	//BestellungController Tests

	@Test
	void getAllTicketsInBestellung() throws Exception {
		benutzer = benutzerRepository.findByUsername("Moritz").get();
		Assertions.assertFalse(benutzer == null);
		Bestellung bestellung1 = new Bestellung();
		bestellungRepository.save(bestellung1);
		bestellung1.setBenutzer(benutzer);


		Bestellung bestellung2 = new Bestellung();
		bestellungRepository.save(bestellung2);
		bestellung2.setBenutzer(benutzer);


		Ticket t1 = new Ticket();
		ticketRepository.save(t1);
		t1.setKaeufer(benutzer);
		t1.setGast(benutzer);
		t1.setBestellung(bestellung1);

		Ticket t2 = new Ticket();
		ticketRepository.save(t2);
		t2.setKaeufer(benutzer);
		t2.setGast(benutzer);
		t2.setBestellung(bestellung1);

		Ticket t3 = new Ticket();
		ticketRepository.save(t3);
		t3.setKaeufer(benutzer);
		t3.setGast(benutzer);
		t3.setBestellung(bestellung2);

		Ticket t4 = new Ticket();
		ticketRepository.save(t4);
		t4.setKaeufer(benutzer);
		t4.setGast(benutzer);
		t4.setBestellung(bestellung2);


		ticketRepository.save(t1);
		ticketRepository.save(t2);
		ticketRepository.save(t3);
		ticketRepository.save(t4);
		bestellungRepository.save(bestellung1);
		bestellungRepository.save(bestellung2);
		mockMvc.perform(MockMvcRequestBuilders.get("/bestellung").header("Authorization", token)).andExpect(status().isOk()).andExpect(content().string(containsString(""+t1.getId())));
	}

	//End BestellungController Tests
	@Test
	public void getAllFilms() throws Exception {
		// this.mockMvc.perform(MockMvcRequestBuilders.get("/bazahlen/bestellung/151/iban/DE-12345678901234567890"))
		ArrayList<String> filmNamen = new ArrayList<String>();
		long size = filmrepository.count();
		filmNamen.add("name");
		filmrepository.findAll().forEach(f -> {
			filmNamen.add(f.getName());
		});
		this.mockMvc.perform(MockMvcRequestBuilders.get("/film/all")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))));
	}

	@Test
	public void getAFilm(){
		// this.mockMvc.perform(MockMvcRequestBuilders.get("/bazahlen/bestellung/151/iban/DE-12345678901234567890"))
		ArrayList<Film> filme = new ArrayList<Film>();
		filmrepository.findAll().forEach(f -> {
			filme.add(f);
		});
		filme.stream().forEach(f -> {
			String call = "/film/" + f.getId();
			try {
				mockMvc.perform(MockMvcRequestBuilders.get(call)).andDo(print()).andExpect(status().isOk())
						.andExpect(content().string(containsString(f.getName())))
						.andExpect(content().string(containsString(f.getBeschreibung())))
						.andExpect(content().string(containsString(((Integer)f.getBewertung()).toString())))
						.andExpect(content().string(containsString(f.getGenre1().toString())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
/*
	@Test
	public void addTicket() throws Exception {
		// TODO function throws error 500
		Ticket ticket = new Ticket();
		ticket.setKaeufer(benutzer);
		ticket.setGast(benutzer);
		ticketRepository.save(ticket);

		String call = "/warenkorb/ticket/" + ticket.getId();;
		try {
			mockMvc.perform(MockMvcRequestBuilders.get(call).header("Authorization", token)).andDo(print()).andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assertions.assertEquals(benutzer.getWarenkorb().getId(), ticketRepository.findById(ticket.getId()).get().getWarenkorb().getId());
		ticketRepository.delete(ticket);
	}*/
/*
	@Test
	public void practice() throws Exception {
		// this.mockMvc.perform(MockMvcRequestBuilders.get("/bazahlen/bestellung/151/iban/DE-12345678901234567890"))
		String call = "/film/";
		mockMvc.perform(MockMvcRequestBuilders.post(call,)).
		
		
		filme.stream().forEach(f -> {
			String call = "/film/" + f.getId();
			try {
				mockMvc.perform(MockMvcRequestBuilders.get(call)).andDo(print()).andExpect(status().isOk())
						.andExpect(content().string(containsString(f.getName())))
						.andExpect(content().string(containsString(f.getBeschreibung())))
						.andExpect(content().string(containsString(((Integer)f.getBewertung()).toString())))
						.andExpect(content().string(containsString(f.getGenre1().toString())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
	
*/
}
