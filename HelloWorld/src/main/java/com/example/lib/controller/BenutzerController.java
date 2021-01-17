package com.example.lib.controller;

import com.example.lib.Benutzer;
import com.example.lib.Repositories.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*") //TODO für localhost Reat einstellen
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
        user.setPasswortHash(bCryptPasswordEncoder.encode(user.getPasswortHash()));
        benutzerRepository.save(user);
        return new ResponseEntity<Object>(user, HttpStatus.OK);
    } //TODO add checks for user
}
