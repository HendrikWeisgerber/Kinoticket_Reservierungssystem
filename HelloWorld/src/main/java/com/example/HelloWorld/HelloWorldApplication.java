package com.example.HelloWorld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.lib.Film;
import com.example.lib.Vorstellung;

@SpringBootApplication
@RestController
public class HelloWorldApplication {
	private static Map<Integer, Film> filme = new HashMap<>();
	private static Map<Integer, Vorstellung> vorstellungen = new HashMap<>();
	static{
		Film starWars = new Film("Star Wars", 1, "the picture is here", "Its cool", 10, 140, 12, true, null);
		Film starWars2 = new Film("Star Wars2", 2, "the picture is here", "Its cool", 10, 140, 12, true, null);
		filme.put(starWars.getId(),starWars);
		filme.put(starWars2.getId(),starWars2);
	}
//Alle Http-Anfragen zu Film
	@RequestMapping(value = "/", produces = "application/json")
	public ResponseEntity<Object> getFilme(){
		return new ResponseEntity<>(filme, HttpStatus.OK);
	}
	//PUT
	@RequestMapping(value = "/film/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateFilm(@PathVariable("id") Integer id, @RequestBody Film film) { 
		filme.remove(id);
		film.setId(id);
		filme.put(id, film);
	   return new ResponseEntity<>("Product is updated successsfully", HttpStatus.OK);
	}   
	//POST
	@RequestMapping(value = "/film", method = RequestMethod.POST)
   public ResponseEntity<Object> postFilm(@RequestBody Film film) {
      filme.put(film.getId(), film);
      return new ResponseEntity<>("Product is created successfully", HttpStatus.CREATED);
   }
   //DEACTIVATE
   @RequestMapping(value = "/film/{id}", method = RequestMethod.DELETE)
   public ResponseEntity<Object> deleteFilm(@PathVariable("id") Integer id) { 
      filme.get(id).setAktiv(false);;
      return new ResponseEntity<>("Product is deleted successsfully", HttpStatus.OK);
   }
//Alle Http Anfragen zu Vorstellungen
	@RequestMapping(value = "/vorstellung", method = RequestMethod.POST)
	public ResponseEntity<Object> postVorstellung(@RequestBody Vorstellung vorstellung) {
		Film filmAlt = filme.get(vorstellung.getFilmId());
		filmAlt.addVorstellung(vorstellung);
		filme.remove(vorstellung.getFilmId());
		//filmAlt.setId(vorstellung.getFilmId());
		filme.put(vorstellung.getFilmId(), filmAlt);
	return new ResponseEntity<>("Product is created successfully", HttpStatus.CREATED);
	}
	public static void main(String[] args) {
		//SpringApplication.run(HelloWorldApplication.class, args);
		SpringApplication app =new SpringApplication(HelloWorldApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		app.run(args);

	}
}
