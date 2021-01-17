package com.example.lib.security;

import com.example.lib.Benutzer;
import com.example.lib.Repositories.BenutzerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/benutzer")
public class BenutzerController {

    private BenutzerRepository benutzerRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup")
    public void signUp(@RequestBody Benutzer user) {
        System.out.println("Sign up called");
        user.setPasswortHash(bCryptPasswordEncoder.encode(user.getPassword()));
        benutzerRepository.save(user);
    }
}
