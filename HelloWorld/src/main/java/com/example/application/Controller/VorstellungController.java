package com.example.application.Controller;

import com.example.application.Application;
import com.example.lib.Film;
import com.example.lib.Vorstellung;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vorstellung")
public class VorstellungController {
    Application helfer = new Application();

    //Alle Http Anfragen zu Vorstellungen   
   @RequestMapping(value = "/", method = RequestMethod.POST)
   public ResponseEntity<Object> postVorstellung(@RequestBody Vorstellung vorstellung) {
       Film filmAlt = helfer.getAktFilme().get(vorstellung.getFilmId());
       filmAlt.addVorstellung(vorstellung);
       helfer.getAktFilme().remove(vorstellung.getFilmId());
       //filmAlt.setId(vorstellung.getFilmId());
       helfer.getAktFilme().put(vorstellung.getFilmId(), filmAlt);
   return new ResponseEntity<>("Product is created successfully", HttpStatus.CREATED);
   }
}
