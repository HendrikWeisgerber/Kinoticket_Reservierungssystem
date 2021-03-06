package com.example.lib.controller;

import com.example.lib.*;
import com.example.lib.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.lib.HelloWorldApplication.isUserAdminOrOwner;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/vorstellung")
public class VorstellungController {

    @Autowired
    SitzRepository sitzRepository;

    @Autowired
    KinosaalRepository kinosaalRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @RequestMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> getVorstellung() {

        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaal(saal);
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        } // TODO extra Methoden und Analog bei anderen Klassen aufrufen oder unnötig und mit API Calls ersetzen?
        /*Vorstellung testVor = new Vorstellung();
        Film filmT = new Film();
        filmRepository.save(filmT);

        testVor.setFilm(filmT);
        vorstellungRepository.save(testVor);*/
        Iterable<Vorstellung> vorstellungIterable = vorstellungRepository.findAll();
        ArrayList<Vorstellung> aktiveVorstellungen = new ArrayList<>();
        ArrayList<Vorstellung> inaktiveVorstellungen = new ArrayList<>();
        ArrayList<ArrayList<Vorstellung>> vorstellungen = new ArrayList<>();
        for (Vorstellung vorstellung : vorstellungIterable) {
            if (vorstellung.isAktiv()) {
                aktiveVorstellungen.add(vorstellung);
            } else {
                inaktiveVorstellungen.add(vorstellung);
            }
        }
        vorstellungen.add(aktiveVorstellungen);
        vorstellungen.add(inaktiveVorstellungen);
        return new ResponseEntity<>(vorstellungen, HttpStatus.OK);
        //return new ResponseEntity<>(vorstellungRepository.findByFilmId((int)film_id),HttpStatus.OK);
    }

    @RequestMapping(value = "/film/{film_id}", produces = "application/json")
    public ResponseEntity<Object> getVorstellungByFilm(@PathVariable(value = "film_id") int film_id) {

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

        Vorstellung[] vorstellungen = vorstellungRepository.findByFilmId(film_id);
        ArrayList<ArrayList<Vorstellung>> alleVorstellungen= new ArrayList<>();
        ArrayList<Vorstellung> aktiveVorstellungen = new ArrayList<>();
        ArrayList<Vorstellung> inaktiveVorstellungen = new ArrayList<>();
        for (Vorstellung vorstellung: vorstellungen) {
            if (vorstellung.isAktiv()) {
                aktiveVorstellungen.add(vorstellung);
            } else {
                inaktiveVorstellungen.add(vorstellung);
            }
        }
        alleVorstellungen.add(aktiveVorstellungen);
        alleVorstellungen.add(inaktiveVorstellungen);

        return new ResponseEntity<>(aktiveVorstellungen, HttpStatus.OK);
    }

    @RequestMapping(value = "/insert", produces = "application/json", method = POST)
    public ResponseEntity<Object> setVorstellung(@RequestBody() Vorstellung vorstellung, Principal principal) {

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Keine Benutzer gefunden", HttpStatus.FORBIDDEN);
        Benutzer benutzer = optionalBenutzer.get();
        if (!isUserAdminOrOwner(benutzer))
            return new ResponseEntity<>("Keine Admin Berechtigung", HttpStatus.FORBIDDEN);

        AtomicBoolean konflikt = new AtomicBoolean(false);
        vorstellungRepository.findAll().forEach(f -> {
            if (f.getSaal() != null && vorstellung.getSaal() != null
                    && f.getSaal().getId() == vorstellung.getSaal().getId()){
                Film fFilm = f.getFilm();
                Film vFilm = f.getFilm();

                if(fFilm != null && vFilm != null){
                    long fEndZeit = f.getStartZeit().getTime() + fFilm.getLaenge()*60*1000;
                    long fStartZeit = f.getStartZeit().getTime();
                    long vEndZeit = vorstellung.getStartZeit().getTime() + vFilm.getLaenge()*60*1000;
                    long vStartZeit = vorstellung.getStartZeit().getTime();
                    if(vStartZeit < fEndZeit && vEndZeit > fStartZeit){
                        konflikt.set(true);
                    }
                    if(fStartZeit < vEndZeit && fEndZeit > vStartZeit){
                        konflikt.set(true);
                    }

                }

            }
        });

        if(konflikt.get()){
            return new ResponseEntity<>("Keine 2 vorstellungen im selben Saal am selben Tag", HttpStatus.OK);
        }

        Optional<Kinosaal> kinosaal = kinosaalRepository.findById(vorstellung.getSaal().getId());
        //Optional<Film> film = filmRepository.findById(vorstellung.getFilm().getId());

        String response = "";
        vorstellungRepository.save(vorstellung);
        response += "Vorstellung hinzugefügt \n";

        if (kinosaal.isEmpty()) {
            kinosaalRepository.save(vorstellung.getSaal());
            response += "Kinosaal nicht gefunden, wurde erstellt";
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/insert/film/{film_id}/kinosaal/{kinosaal_id}/startzeit/{startzeit}/grundpreis/{grundpreis}/aktiv/{aktiv}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setVorstellung(@PathVariable(value = "kinosaal_id") long kinosaal_id,
                                                 @PathVariable(value = "film_id") long film_id,
                                                 @PathVariable(value = "startzeit") @DateTimeFormat(pattern = "MMddyyyyHHmm") Date startzeit,
                                                 @PathVariable(value = "grundpreis") BigDecimal grundpreis,
                                                 @PathVariable(value = "aktiv") long aktiv,
                                                 Principal principal) {

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Keine Benutzer gefunden", HttpStatus.FORBIDDEN);
        Benutzer benutzer = optionalBenutzer.get();
        if (!isUserAdminOrOwner(benutzer))
            return new ResponseEntity<>("Keine Admin Berechtigung", HttpStatus.FORBIDDEN);

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

    @RequestMapping(value = "/{vorstellung_id}/aktiv/{aktiv}", produces = "application/json", method = POST)
    public ResponseEntity<Object> setVorstellungAktiv(@PathVariable(value = "vorstellung_id") long vorstelung_id,
                                                      @PathVariable(value = "aktiv") long aktiv,
                                                      Principal principal) {

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Keine Benutzer gefunden", HttpStatus.FORBIDDEN);
        Benutzer benutzer = optionalBenutzer.get();
        if (!isUserAdminOrOwner(benutzer))
            return new ResponseEntity<>("Keine Admin Berechtigung", HttpStatus.FORBIDDEN);

        Optional<Vorstellung> optionalVorstellung = vorstellungRepository.findById((int) vorstelung_id);
        if (optionalVorstellung.isEmpty()) return new ResponseEntity<>("Keine Vorstellung", HttpStatus.OK);
        Vorstellung vorstellung = optionalVorstellung.get();
        boolean isAktiv = aktiv == 1;
        vorstellung.setAktiv(isAktiv);
        vorstellungRepository.save(vorstellung);

        return new ResponseEntity<>(vorstellung, HttpStatus.OK);
    }
}
