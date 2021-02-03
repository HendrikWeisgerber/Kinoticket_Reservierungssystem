package com.example.HelloWorld;

import com.example.lib.*;
import com.example.lib.Enum.EssenSorte;
import com.example.lib.Enum.GetraenkeSorte;
import com.example.lib.Enum.Groesse;
import com.example.lib.Repositories.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.example.lib.Enum.Preiskategorie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest(classes = { com.example.lib.HelloWorldApplication.class })
@AutoConfigureMockMvc
class HelloWorldApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FilmRepository filmRepository;

	@Autowired
	private BenutzerRepository benutzerRepository;

	@Autowired
	private WarenkorbRepository warenkorbRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private BestellungRepository bestellungRepository;

	@Autowired
	private KinosaalRepository kinosaalRepository;

	@Autowired
	private VorstellungRepository vorstellungRepository;

	@Autowired
	private SitzRepository sitzRepository;

	@Autowired
	private SnackRepository snackRepository;

	@Autowired
	private GetraenkRepository getraenkRepository;

	private Benutzer benutzer;

	private String token;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	public static HashMap<String,String> jsonToMap(String t) throws JSONException {

		HashMap<String, String> map = new HashMap<String, String>();
		JSONObject jObject = new JSONObject(t);
		Iterator<?> keys = jObject.keys();

		while( keys.hasNext() ){
			String key = (String)keys.next();
			String value = jObject.getString(key);
			map.put(key, value);

		}
		return map;
	}

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
	//FilmController Tests
	@Test
	public void getAllFilms() throws Exception {
		// this.mockMvc.perform(MockMvcRequestBuilders.get("/bazahlen/bestellung/151/iban/DE-12345678901234567890"))
		ArrayList<String> filmNamen = new ArrayList<String>();
		long size = filmRepository.count();
		filmNamen.add("name");
		filmRepository.findAll().forEach(f -> {
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
	public void getFilmById(){
		// this.mockMvc.perform(MockMvcRequestBuilders.get("/bazahlen/bestellung/151/iban/DE-12345678901234567890"))
		ArrayList<Film> filme = new ArrayList<Film>();
		filmRepository.findAll().forEach(f -> {
			filme.add(f);
		});
		filme.stream().forEach(f -> {
			String call = "/film/" + f.getId();
			try {
				mockMvc.perform(MockMvcRequestBuilders.get(call)).andDo(print()).andExpect(status().isOk())
						.andExpect(content().string(containsString(f.getName())))
						.andExpect(content().string(containsString(f.getBeschreibung().split("[Ä,ä,Ö,ö,Ü,ü,ß]")[0])))
						.andExpect(content().string(containsString(((Integer)f.getBewertung()).toString())))
						.andExpect(content().string(containsString(f.getGenre1().toString())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void postNewFilm() throws Exception {

		filmRepository.findAll().forEach(f -> {
			if (f.getName().equals("Finding Nemo but only for testpurposes")) {
				filmRepository.delete(f);
			}
		});
		long counter = filmRepository.count();
		HashMap<String, String> hashFilm = new HashMap<>();
		String body = "{\n" +
				"        \"Filmid\": \"tt0266543\",\n" +
				"        \"actor0\": \"Albert Brooks\",\n" +
				"        \"actor0Cara\": \"Marlin (voice)\",\n" +
				"        \"actor1\": \"Ellen DeGeneres\",\n" +
				"        \"actor1Cara\": \"Dory (voice)\",\n" +
				"        \"actor2\": \"Alexander Gould\",\n" +
				"        \"actor2Cara\": \"Nemo (voice)\",\n" +
				"        \"awards\": \"Top Rated Movies #170 | Won 1 Oscar. Another 49 wins & 62 nominations.\",\n" +
				"        \"directors\": \"Andrew Stanton, Lee Unkrich\",\n" +
				"        \"genreList\": [\n" +
				"                    {\n" +
				"                        \"key\": \"Animation\",\n" +
				"                        \"value\": \"Animation\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"key\": \"Adventure\",\n" +
				"                        \"value\": \"Adventure\"\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"key\": \"Comedy\",\n" +
				"                        \"value\": \"Comedy\"\n" +
				"                    },\n" +
				"                {\n" +
				"                        \"key\": \"Family\",\n" +
				"                        \"value\": \"Family\"\n" +
				"                    }\n" +
				"                ],\n" +
				"        \"image\": \"https://imdb-api.com/images/original/MV5BZTAzNWZlNmUtZDEzYi00ZjA5LWIwYjEtZGM1NWE1MjE4YWRhXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_Ratio0.6791_AL_.jpg\",\n" +
				"        \"plotLocal\": \"Im Schutze eines großen Riffs lebt die Familie von Clownsfisch Marlin relativ sicher und trotzdem werden seine Frau und nahezu alle seine Kinder Opfer eines gefräßigen Räubers. Das einzige ihm verbleibende Kind ist der kleine Nemo, der von da an besonders behütet wird. Doch die Neugier ist zu groß und prompt wird Nemo gefangen und zu den Menschen als Zierfisch gebracht. Durch ein paar Möwen erfährt Marlin alsbald, wo Nemo gelandet ist: im Aquarium eines Zahnarztes in Sydney. Mutig macht sich der Clownsfish mit seiner Freundin Dory, die allerdings ständig an Gedächtnisverlust leidet auf den Weg dorthin, wobei an Haien, Quallen und anderen Gefahren. Währenddessen ist Nemo nicht untätig und plant mit den anderen Aquariumsfischen einen gewagten Ausbruch aus ihrem Gefängnis...\",\n" +
				"        \"runTime\": \"100\",\n" +
				"        \"title\": \"Finding Nemo but only for testpurposes\"\n" +
				"    }";
		hashFilm = jsonToMap(body);
		mockMvc.perform(MockMvcRequestBuilders.post("/film/").header("Authorization", token).contentType(APPLICATION_JSON_UTF8).content(body)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("Der Film wurde hinzugefügt!");
		Assertions.assertEquals(counter + 1, filmRepository.count());
		filmRepository.findAll().forEach(f -> {
			if (f.getName().equals("Finding Nemo but only for testpurposes")) {
				filmRepository.delete(f);
			}
		});
	}
	//End FilmController Tests
	//KinosaalController Tests
	@Test
	public void getSaalByVorstellung() throws Exception {

		Vorstellung vorstellung = new Vorstellung();
		Kinosaal saal = new Kinosaal();
		saal.setReihe(10);
		saal.setSpalte(10);
		saal.setName("Kinosaal for UnitTesting only");
		vorstellung.setSaal(saal);
		kinosaalRepository.save(saal);
		vorstellungRepository.save(vorstellung);
		String call = "/kinosaal/vorstellung/" + vorstellung.getId();
		mockMvc.perform(MockMvcRequestBuilders.get(call).header("Authorization", token)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString().contains("Kinosaal for UnitTesting only");
		vorstellungRepository.delete(vorstellung);
		kinosaalRepository.delete(saal);
	}
	//End KinosaalController Tests
	//SitzController Tests
	@Test
	public void getAllSitzeBelegt() throws Exception {
		//benutzerRepository.save(benutzer);
		benutzer = benutzerRepository.findByUsername("Moritz").get();
		Vorstellung vorstellung = new Vorstellung();
		Kinosaal saal = new Kinosaal((int) (Math.random() * 10 + 1), (int) (Math.random() * 10 + 1));
		saal.setName("Unittest saal");

		Sitz[][] sitze = new Sitz[saal.getReihe()][saal.getSpalte()];
		kinosaalRepository.save(saal);
		vorstellung.setSaal(saal);
		vorstellungRepository.save(vorstellung);
		for (int i = 0; i < saal.getReihe(); i++) {
			for (int j = 0; j < saal.getSpalte(); j++) {
				sitze[i][j] = new Sitz();
				sitze[i][j].setKinosaal(saal);
				sitze[i][j].setReihe(i);
				sitze[i][j].setSpalte(j);
				sitze[i][j].setPreisschluessel(new BigDecimal(1));
				sitze[i][j].setBarriereFrei(false);
				sitzRepository.save(sitze[i][j]);
			}
		}

		String call = "/sitz/vorstellung/" + vorstellung.getId();
		String content = mockMvc.perform(MockMvcRequestBuilders.get(call).header("Authorization", token)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		for (Sitz[] reihe : sitze) {
			for (Sitz sitz : reihe) {
				Assertions.assertTrue(content.contains("" + sitz.getId()));
			}
		}
		for (Sitz[] reihe : sitze) {
			for (Sitz sitz : reihe) {
				sitzRepository.delete(sitz);
			}
		}
		vorstellungRepository.delete(vorstellung);
		kinosaalRepository.delete(saal);
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
