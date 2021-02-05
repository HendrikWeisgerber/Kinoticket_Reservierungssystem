package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Kinosaal;
import com.example.lib.Repositories.BenutzerRepository;
import com.example.lib.Repositories.KinosaalRepository;
import com.example.lib.Repositories.SitzRepository;
import com.example.lib.Repositories.VorstellungRepository;
import com.example.lib.Sitz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static com.example.lib.HelloWorldApplication.isUserAdminOrOwner;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/kinosaal")
public class KinosaalController {

    @Autowired
    KinosaalRepository kinosaalRepository;

    @Autowired
    SitzRepository sitzRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @RequestMapping(value = "/all", produces = "application/json")
    public ResponseEntity<Object> getAllSaal(){
        return new ResponseEntity<>(kinosaalRepository.findAll(), HttpStatus.OK);
    }
    @RequestMapping(value = "/vorstellung/{vorstellung_id}", produces = "application/json")
    public ResponseEntity<Object> getSaalByVorstellung(@PathVariable(value = "vorstellung_id") int vorstellung_id) {

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

        if (vorstellungRepository.findById(vorstellung_id).isEmpty()) {
            return new ResponseEntity<>("Keine Vorstellung", HttpStatus.OK);
        }

        return new ResponseEntity<>(vorstellungRepository.findById(vorstellung_id).get().getSaal(), HttpStatus.OK);
    }


    @RequestMapping(value = "/insert", produces = "application/json", method = POST)
    public ResponseEntity<Object> setSaal(@RequestBody() Kinosaal kinosaal, Principal principal) {
        if (kinosaal.getReihe() == 0 || kinosaal.getSpalte() == 0) {
            return new ResponseEntity<>("Reihe und Spalte können nicht 0 sein", HttpStatus.OK);
        }

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isEmpty()) return new ResponseEntity<>("Keine Benutzer gefunden", HttpStatus.FORBIDDEN);
        Benutzer benutzer = optionalBenutzer.get();
        if (!isUserAdminOrOwner(benutzer))
            return new ResponseEntity<>("Keine Admin Berechtigung", HttpStatus.FORBIDDEN);

        kinosaalRepository.save(kinosaal);
        return new ResponseEntity<>(kinosaal, HttpStatus.OK);
    }

}
