package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Enum.Preiskategorie;
import com.example.lib.Enum.Rechte;
import com.example.lib.Enum.Zone;
import com.example.lib.Repositories.BenutzerRepository;
import com.example.lib.Repositories.WarenkorbRepository;
import com.example.lib.Warenkorb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static com.example.lib.HelloWorldApplication.isUserAdminOrOwner;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*", allowedHeaders = "*") //TODO für localhost React einstellen, Sicherheit
@RestController
@RequestMapping(value = "/benutzer")
public class BenutzerController {

    @Autowired
    private BenutzerRepository benutzerRepository;

    @Autowired
    private WarenkorbRepository warenkorbRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/signup", produces = "application/json", method = POST)
    public ResponseEntity<Object> signUp(@RequestBody Benutzer user, Principal principal) {
        System.out.println("Sign up called");

        if (user.getUsername().isEmpty() || user.getUsername() == null || user.getPasswortHash().isEmpty() || user.getPasswortHash() == null) {
            return new ResponseEntity<>("Der Benutzername oder das Passwort sind leer", HttpStatus.OK);
        }

        if (benutzerRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("Der Benutzername ist bereits vergeben", HttpStatus.OK);
        }

        if (isUserAdminOrOwner(user)) {
            Optional<Benutzer> ownerOptional = benutzerRepository.findByUsername(principal.getName());
            if (ownerOptional.isEmpty())
                return new ResponseEntity<>("Loggen Sie sich als Owner ein", HttpStatus.FORBIDDEN);
            Benutzer owner = ownerOptional.get();
            if (!owner.getRechte().toString().toLowerCase().equals(Rechte.OWNER.toString()))
                return new ResponseEntity<>("Sie haben keine Admin Berechtigung", HttpStatus.FORBIDDEN);
        } else {
            //Standard
            user.setRechte(Rechte.USER);
        }

        if (user.getPreiskategorie() == null || user.getPreiskategorie().toString().isEmpty()) {
            user.setPreiskategorie(Preiskategorie.ERWACHSENER); // Falls nicht dabei, automatisch Erwachsener
        }

        Warenkorb w = new Warenkorb();
        w.setBenutzer(user);

        user.setPasswortHash(bCryptPasswordEncoder.encode(user.getPasswortHash()));
        benutzerRepository.save(user);
        warenkorbRepository.save(w);
        return new ResponseEntity<>(user, HttpStatus.OK);
    } //TODO add more checks for user

    @RequestMapping(value = "/update", produces = "application/json", method = POST)
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
        if (user.getPreiskategorie() != null) {
            b.setPreiskategorie(user.getPreiskategorie());
        }
        benutzerRepository.save(b);

        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @RequestMapping(value = "", produces = "application/json", method = GET)
    public ResponseEntity<Object> getUser(Principal principal) {
        Optional<Benutzer> benutzerOptional = benutzerRepository.findByUsername(principal.getName());
        Benutzer b;
        if (benutzerOptional.isEmpty()) {
            return new ResponseEntity<>("Kein Benutzer mit dem Benutzernamen", HttpStatus.OK);
        }

        b = benutzerOptional.get();
        b.setPasswortHash("");
        b.setWarenkorb(null);

        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @RequestMapping(value = "/role", produces = "application/json", method = POST)
    public ResponseEntity<Object> getUserRole(Principal principal) {
        Optional<Benutzer> benutzerOptional = benutzerRepository.findByUsername(principal.getName());
        Benutzer b;
        if (benutzerOptional.isEmpty()) {
            return new ResponseEntity<>("Kein Benutzer mit dem Benutzernamen", HttpStatus.OK);
        }
        b = benutzerOptional.get();
        String response;
        if (b.getRechte() != null) response = b.getRechte().toString();
        else {
            b.setRechte(Rechte.USER);
            benutzerRepository.save(b);
            response = Rechte.USER.toString();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/lieblingszone", produces = "application/json", method = POST)
    public ResponseEntity<Object> getUserLeiblingsZone(Principal principal) {
        Optional<Benutzer> benutzerOptional = benutzerRepository.findByUsername(principal.getName());
        Benutzer b;
        if (benutzerOptional.isEmpty()) {
            return new ResponseEntity<>("Kein Benutzer mit dem Benutzernamen", HttpStatus.OK);
        }
        b = benutzerOptional.get();
        Zone zone = b.getLieblingszone();
        if (zone == null || zone.toString().isEmpty())
            return new ResponseEntity<>("Keine Lieblingszone", HttpStatus.OK);

        return new ResponseEntity<>(zone.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/preiskategorie", produces = "application/json", method = POST)
    public ResponseEntity<Object> getUserPreiskategorie(Principal principal) {
        Optional<Benutzer> benutzerOptional = benutzerRepository.findByUsername(principal.getName());
        Benutzer b;
        if (benutzerOptional.isEmpty()) {
            return new ResponseEntity<>("Kein Benutzer mit dem Benutzernamen", HttpStatus.OK);
        }
        b = benutzerOptional.get();
        Preiskategorie preiskategorie = b.getPreiskategorie();
        if (preiskategorie == null || preiskategorie.toString().isEmpty())
            return new ResponseEntity<>("Keine Preiskategorie", HttpStatus.OK);

        return new ResponseEntity<>(preiskategorie.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/reset/username/{username}/password/{password}", produces = "application/json", method = POST)
    public ResponseEntity<Object> resetPasswort(@PathVariable(value = "username") String username,
                                                @PathVariable(value = "password") String password) {
        Optional<Benutzer> benutzerOptional = benutzerRepository.findByUsername(username);
        Benutzer b;
        if (benutzerOptional.isEmpty()) {
            return new ResponseEntity<>("Kein Benutzer mit dem Benutzernamen", HttpStatus.OK);
        }
        b = benutzerOptional.get();
        b.setPasswortHash(bCryptPasswordEncoder.encode(password));

        return new ResponseEntity<>("Passwort geändert", HttpStatus.OK);
    }
}
