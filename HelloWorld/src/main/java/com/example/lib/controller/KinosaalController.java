package com.example.lib.controller;

import com.example.lib.Kinosaal;
import com.example.lib.Repositories.KinosaalRepository;
import com.example.lib.Repositories.SitzRepository;
import com.example.lib.Repositories.VorstellungRepository;
import com.example.lib.Sitz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
