package com.example.lib.controller;

import com.example.lib.*;
import com.example.lib.Enum.Genre;
import com.example.lib.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static com.example.lib.HelloWorldApplication.isUserAdminOrOwner;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/film")
public class FilmController {

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    KinosaalRepository kinosaalRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @Autowired
    SitzRepository sitzRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/all", produces = "application/json")
    public ResponseEntity<Object> getAllFilms() {

        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaal(saal);
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        }

        Iterable<Film> alleFilme = filmRepository.findAll();
        for (Film film : alleFilme) {
            Vorstellung[] vorstellungen = vorstellungRepository.findByFilmId(film.getId());
            for (Vorstellung vorstellung : vorstellungen) {
                if (film.getVorstellung() == null) {
                    film.setVorstellung();
                }
                film.getVorstellung().add(vorstellung);
            }
        }

        return new ResponseEntity<>(filmRepository.findAll(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/{film_id}", produces = "application/json")
    public ResponseEntity<Object> getFilmbyID(@PathVariable(value = "film_id") int film_id) {

        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaal(saal);
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        }

        Optional<Film> film = filmRepository.findById(film_id);
        if (film.isEmpty()) {
            return new ResponseEntity<Object>("Kein Film mit der Id: " + film_id, HttpStatus.OK);
        }
        Vorstellung[] vorstellungen = vorstellungRepository.findByFilmId(film.get().getId());
        for (Vorstellung vorstellung : vorstellungen) {
            if (film.get().getVorstellung() == null) {
                film.get().setVorstellung();
            }
            film.get().getVorstellung().add(vorstellung);
        }

        return new ResponseEntity<Object>(film, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/", produces = "application/json", method = POST)
    public ResponseEntity<String> postNewFilm(@RequestBody HashMap object, Principal principal) {

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Keine Benutzer gefunden", HttpStatus.FORBIDDEN);
        Benutzer benutzer = optionalBenutzer.get();
        if (!isUserAdminOrOwner(benutzer))
            return new ResponseEntity<>("Keine Admin Berechtigung", HttpStatus.FORBIDDEN);

        HashMap hashFilm = object;
        String name = ((String) hashFilm.get("title"));
        String bild = ((String) hashFilm.get("image"));
        String beschreibung = ((String) hashFilm.get("plotLocal"));
        int bewertung = 9;
        int laenge = (Integer.parseInt((String) hashFilm.get("runTime")));
        ArrayList genreList = (ArrayList) hashFilm.get("genreList");
        String[] genreArray = new String[genreList.size()];
        for (int i = 0; i < genreList.size(); i++) {
            HashMap<String, String> hashGenre = (HashMap<String, String>) genreList.get(i);
            String[] genreString = String.valueOf(hashGenre.entrySet().toArray()[1]).split("=");
            genreArray[i] = genreString[1];
        }
        Genre[] genres = new Genre[3];
        genres[0] = Genre.NO_GENRE;
        genres[1] = Genre.NO_GENRE;
        genres[2] = Genre.NO_GENRE;
        int genreCounter = 0;

        for (String genreName : genreArray) {
            if (genreCounter < genres.length) {
                switch (genreName) { // TODO besser toLowerCase() verwenden, wäre auch weniger Arbeit XD
                    case "Action":
                    case "ACTION":
                    case "action":
                        genres[genreCounter] = Genre.ACTION;
                        genreCounter++;
                        break;
                    case "Comedy":
                    case "COMEDY":
                    case "comedy":
                        genres[genreCounter] = Genre.COMEDY;
                        genreCounter++;
                        break;
                    case "Horror":
                    case "HORROR":
                    case "horror":
                        genres[genreCounter] = Genre.HORROR;
                        genreCounter++;
                        break;
                    case "Sci-Fi":
                    case "SCI-FI":
                    case "sci-fi":
                    case "Sci_Fi":
                    case "SCI_FI":
                    case "sci_fi":
                    case "SciFi":
                    case "SCIFI":
                    case "scifi":
                        genres[genreCounter] = Genre.SCI_FI;
                        genreCounter++;
                        break;
                    case "Thriller":
                    case "THRILLER":
                    case "thriller":
                        genres[genreCounter] = Genre.THRILLER;
                        genreCounter++;
                        break;
                    case "Fantasy":
                    case "FANTASY":
                    case "fantasy":
                        genres[genreCounter] = Genre.FANTASY;
                        genreCounter++;
                        break;
                    case "Drama":
                    case "DRAMA":
                    case "drama":
                        genres[genreCounter] = Genre.DRAMA;
                        genreCounter++;
                        break;
                    case "Documentary":
                    case "DOCUMENTARY":
                    case "documentary":
                        genres[genreCounter] = Genre.DOCUMENTARY;
                        genreCounter++;
                        break;
                    case "History":
                    case "HISTORY":
                    case "history":
                        genres[genreCounter] = Genre.HISTORY;
                        genreCounter++;
                        break;
                    case "Animation":
                    case "ANIMATION":
                    case "animation":
                        genres[genreCounter] = Genre.ANIMATION;
                        genreCounter++;
                        break;
                    case "Family":
                    case "FAMILY":
                    case "family":
                        genres[genreCounter] = Genre.FAMILY;
                        genreCounter++;
                        break;
                    case "Musical":
                    case "MUSICAL":
                    case "musical":
                    case "Music":
                    case "MUSIC":
                    case "music":
                        genres[genreCounter] = Genre.MUSICAL;
                        genreCounter++;
                        break;
                    case "Short":
                    case "SHORT":
                    case "short":
                        genres[genreCounter] = Genre.SHORT;
                        genreCounter++;
                        break;
                    case "Romance":
                    case "ROMANCE":
                    case "romance":
                        genres[genreCounter] = Genre.ROMANCE;
                        genreCounter++;
                        break;
                    case "Superhero":
                    case "SUPERHERO":
                    case "superhero":
                        genres[genreCounter] = Genre.SUPERHERO;
                        genreCounter++;
                        break;
                    case "Western":
                    case "WESTERN":
                    case "western":
                        genres[genreCounter] = Genre.WESTERN;
                        genreCounter++;
                        break;
                    default:
                        genres[genreCounter] = Genre.SONSTIGE;

                }

            }
        }
        Film film = new Film(name, bild, beschreibung, 9, laenge, 12, true, genres[0], genres[1], genres[2]);
        filmRepository.save(film);
        return new ResponseEntity<>("Der Film wurde hinzugefügt!", HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/{film_id}/aktiv/{aktiv}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setFilmAktiv(@PathVariable(value = "film_id") int film_id,
                                               @PathVariable(value = "aktiv") int aktiv,
                                               Principal principal) {

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Keine Benutzer gefunden", HttpStatus.FORBIDDEN);
        Benutzer benutzer = optionalBenutzer.get();
        if (!isUserAdminOrOwner(benutzer))
            return new ResponseEntity<>("Keine Admin Berechtigung", HttpStatus.FORBIDDEN);

        Optional<Film> optionalFilm = filmRepository.findById(film_id);
        if (optionalFilm.isEmpty()) {
            return new ResponseEntity<Object>("Kein Film mit der Id: " + film_id, HttpStatus.OK);
        }

        Film film = optionalFilm.get();
        boolean isAktiv = aktiv == 1;
        String aktivString = isAktiv ? "aktiv" : "inaktiv";
        film.setAktiv(isAktiv);
        filmRepository.save(film);
        Vorstellung[] vorstellungen = vorstellungRepository.findByFilmId(film.getId());
        if (vorstellungen == null) return new ResponseEntity<>("Film auf " + aktivString + " gesetzt", HttpStatus.OK);
        for (Vorstellung vorstellung : vorstellungen) {
            vorstellung.setAktiv(isAktiv);
            vorstellungRepository.save(vorstellung);
        }

        return new ResponseEntity<>("Film und alle dazugehörigen Vorstellungen wurden auf " + aktivString + "gesetzt", HttpStatus.OK);
    }
}
