package com.example.lib.security;

import com.example.lib.Benutzer;
import com.example.lib.Repositories.BenutzerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

// Die Klasse wird momentan nicht verwendet, weil es in HelloWorld direkt implementiert ist. Evtl. aber diese benutzen,
// falls die Implementierung größer wird
public class UserDetailsServiceImpl implements UserDetailsService {
    private BenutzerRepository benutzerRepository;


    public UserDetailsServiceImpl(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Benutzer> benutzerOptional = benutzerRepository.findByUsername(username);
        Benutzer user;
        if (benutzerOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        user = benutzerOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswortHash(), Collections.emptyList());
    }


}
