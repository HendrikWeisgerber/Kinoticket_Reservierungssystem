package com.example.lib.controller;

import com.example.lib.Film;
import com.example.lib.Repositories.FilmRepository;
import com.example.lib.Repositories.VorstellungRepository;
import com.example.lib.Vorstellung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/film")
public class FilmController {

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @RequestMapping(value= "/all", produces ="application/json")
    public ResponseEntity<Object> getAllFilms(){

        Iterable<Film> alleFilme = filmRepository.findAll();
        for(Film film: alleFilme) {
            Vorstellung[] vorstellungen = vorstellungRepository.findByFilmId(film.getId());
            for(Vorstellung vorstellung : vorstellungen) {
                if(film.getVorstellung() == null) {
                    film.setVorstellung();
                }
                film.getVorstellung().add(vorstellung);
            }
        }

        return new ResponseEntity<Object>(alleFilme, HttpStatus.OK);
    }


    @RequestMapping(value = "/{film_id}", produces = "application/json")
    public ResponseEntity<Object> getFilmbyID(@PathVariable(value = "film_id")int film_id, SpringDataWebProperties.Pageable pageable){

        Optional<Film> film = filmRepository.findById(film_id);
        Vorstellung[] vorstellungen = vorstellungRepository.findByFilmId(film.get().getId());
        for(Vorstellung vorstellung : vorstellungen) {
            if(film.get().getVorstellung() == null) {
                film.get().setVorstellung();
            }
            film.get().getVorstellung().add(vorstellung);
        }

        return new ResponseEntity<Object>(film, HttpStatus.OK);
    }
}
