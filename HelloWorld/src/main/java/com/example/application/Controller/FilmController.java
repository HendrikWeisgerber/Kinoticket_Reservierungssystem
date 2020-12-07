package com.example.application.Controller;

import com.example.application.Application;
import com.example.lib.Film;
import com.example.lib.Vorstellung;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/film")
public class FilmController {
	Application helfer = new Application();
//Alle Http-Anfragen zu Film
	@RequestMapping(value = "", produces = "application/json")
	public ResponseEntity<Object> getFilme(){
		return new ResponseEntity<>(helfer.getAktFilme(), HttpStatus.OK);
	}
	//Get deaktFilme
	@RequestMapping(value = "/false")
   	public ResponseEntity<Object> getProduct() {
      return new ResponseEntity<>(helfer.getDeaktFilme().values(), HttpStatus.OK);
   }
	//PUT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateFilm(@PathVariable("id") Integer id, @RequestBody Film film) { 
		helfer.getAktFilme().remove(id);
		film.setId(id);
		helfer.getAktFilme().put(id, film);
	   return new ResponseEntity<>("Product is updated successsfully", HttpStatus.OK);
	}   
	//POST
	@RequestMapping(value = "/", method = RequestMethod.POST)
   public ResponseEntity<Object> postFilm(@RequestBody Film film) {
	  helfer.getAktFilme().put(film.getId(), film);
      return new ResponseEntity<>("Product is created successfully", HttpStatus.CREATED);
   }
   //DEACTIVATE
   @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
   public ResponseEntity<Object> deleteFilm(@PathVariable("id") Integer id) { 
	  helfer.getAktFilme().get(id).setAktiv(false);
	  helfer.getDeaktFilme().put(id, helfer.getAktFilme().get(id));
	  helfer.getAktFilme().remove(id);
      return new ResponseEntity<>("Product is deleted successsfully", HttpStatus.OK);
   }
}
