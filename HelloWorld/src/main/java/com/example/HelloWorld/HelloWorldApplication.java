package com.example.HelloWorld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.lib.Movie;

@SpringBootApplication
@RestController
public class HelloWorldApplication {

	@RequestMapping(value = "/", produces = "application/json")
	public ResponseEntity<Object> home(){
		Movie starWars = new Movie("Star Wars", "the picture is here", "Its cool", 10, 140, 12, true);
		Movie starWars2 = new Movie("Star Wars2", "the picture is here", "Its cool", 10, 140, 12, true);
		final Map<String, Movie> movies = new HashMap<>();
		
		movies.put("a",starWars);
		movies.put("b",starWars2);
		return new ResponseEntity<>(starWars, HttpStatus.OK);
	}
	public static void main(String[] args) {
		//SpringApplication.run(HelloWorldApplication.class, args);
		SpringApplication app =new SpringApplication(HelloWorldApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		app.run(args);

	}




}
