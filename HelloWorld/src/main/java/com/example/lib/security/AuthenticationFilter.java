package com.example.lib.security;

import com.example.lib.Benutzer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Benutzer creds = new ObjectMapper().readValue(request.getInputStream(), Benutzer.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPasswortHash(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException("Could not read request" + e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) {
        String token = Jwts.builder()
                .setSubject(((User) authentication.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityCons.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityCons.SECRET.getBytes())
                .compact();

        System.out.println("Token: " + token);
        response.addHeader(SecurityCons.HEADER_STRING, SecurityCons.TOKEN_PREFIX + token);
        try {
            response.getWriter().write(SecurityCons.TOKEN_PREFIX + token);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
