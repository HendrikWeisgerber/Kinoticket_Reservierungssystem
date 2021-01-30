package com.example.lib.controller;

import com.example.lib.Film;
import com.example.lib.Kinosaal;
import com.example.lib.Repositories.FilmRepository;
import com.example.lib.Repositories.KinosaalRepository;
import com.example.lib.Repositories.VorstellungRepository;
import com.example.lib.Vorstellung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/")
public class VorstellungController {

    @Autowired
    KinosaalRepository kinosaalRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @RequestMapping(value = "/insert/vorstellung", produces = "application/json", method = POST)
    public ResponseEntity<Object> setVorstellung(@RequestBody() Vorstellung vorstellung) {
        Optional<Kinosaal> kinosaal = kinosaalRepository.findById(vorstellung.getSaal().getId());
        Optional<Film> film = filmRepository.findById(vorstellung.getFilm().getId());

        String response = "";
        vorstellungRepository.save(vorstellung);
        response += "Vorstellung hinzugefügt \n";

        if (kinosaal.isEmpty()) {
            kinosaalRepository.save(vorstellung.getSaal());
            response += "Kinosaal nicht gefunden, wurde erstellt";
        } //TODO Das gleiche für Film wenn ohne ID!

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/insert/vorstellung/film/{film_id}/kinosaal/{kinosaal_id}/startzeit/{startzeit}/grundpreis/{grundpreis}/aktiv/{aktiv]", produces = "application/json", method = POST)
    public ResponseEntity<Object> setVorstellung(@PathVariable(value = "kinosaal_id") long kinosaal_id,
                                                 @PathVariable(value = "film_id") long film_id,
                                                 @PathVariable(value = "startzeit") @DateTimeFormat(pattern = "MMddyyyyHHmm") Date startzeit,
                                                 @PathVariable(value = "grundpreis") BigDecimal grundpreis,
                                                 @PathVariable(value = "aktiv") long aktiv) {

        Optional<Kinosaal> kinosaal = kinosaalRepository.findById((int) kinosaal_id);
        Optional<Film> film = filmRepository.findById((int) film_id);

        if (kinosaal.isPresent() && film.isPresent()) {
            Vorstellung vorstellung = new Vorstellung();
            vorstellung.setFilm(film.get());
            vorstellung.setSaal(kinosaal.get());
            vorstellung.setStartZeit(startzeit);
            vorstellung.setGrundpreis(grundpreis);
            boolean istAktiv = false;
            istAktiv = (aktiv != 0);
            vorstellung.setAktiv(istAktiv);
            vorstellungRepository.save(vorstellung);

            return new ResponseEntity<>(vorstellung, HttpStatus.OK);
        }

        return new ResponseEntity<>("Kinosaal oder Film nicht gefunden", HttpStatus.OK);
    }
}
