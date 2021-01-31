package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Enum.Preiskategorie;
import com.example.lib.Repositories.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*", allowedHeaders = "*") //TODO für localhost React einstellen, Sicherheit
@RestController
@RequestMapping(value = "/benutzer")
public class BenutzerController {

    @Autowired
    private BenutzerRepository benutzerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody Benutzer user) {
        System.out.println("Sign up called");

        if (user.getUsername().isEmpty() || user.getUsername() == null || user.getPasswortHash().isEmpty() || user.getPasswortHash() == null) {
            return new ResponseEntity<Object>("Der Benutzername oder das Passwort sind leer", HttpStatus.OK);
        }

        if (benutzerRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<Object>("Der Benutzername ist bereits vergeben", HttpStatus.OK);
        }

        if (user.getPreiskategorie() == null || user.getPreiskategorie().toString().isEmpty()) {
            user.setPreiskategorie(Preiskategorie.ERWACHSENER); // Falls nicht dabei, automatisch Erwachsener
        }

        user.setPasswortHash(bCryptPasswordEncoder.encode(user.getPasswortHash()));
        benutzerRepository.save(user);
        return new ResponseEntity<Object>(user, HttpStatus.OK);
    } //TODO add more checks for user

    @RequestMapping(value = "update", produces = "application/json", method = POST)
    public ResponseEntity<Object> updateUser(@RequestBody Benutzer user,
                                             Principal principal) {
        Optional<Benutzer> benutzerOptional = benutzerRepository.findByUsername(principal.getName());
        Benutzer b;
        if (benutzerOptional.isEmpty()) {
            return new ResponseEntity<>("Kein Benutzer mit dem Benutzernamen", HttpStatus.OK);
        }

        b = benutzerOptional.get();
        b.setVorname(user.getVorname());
        b.setNachname(user.getNachname());
        b.setEmail(user.getEmail());
        b.setNewsletter(user.getNewsletter());
        benutzerRepository.save(b);

        return new ResponseEntity<>(b, HttpStatus.OK);
    }
}
