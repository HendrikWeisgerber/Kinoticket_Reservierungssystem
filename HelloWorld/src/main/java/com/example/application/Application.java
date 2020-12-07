package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.lib.Film;

@SpringBootApplication
public class Application {
	private static Map<Integer, Film> aktFilme = new HashMap<Integer, Film>();
	private static Map<Integer, Film> deaktFilme = new HashMap<Integer, Film>();
	static{
		Film starWars = new Film("Star Wars", 1, "the picture is here", "Its cool", 9, 140, 12, true, null);
		Film starWars2 = new Film("Star Wars 2", 2, "the picture is here", "Its cool", 8, 140, 12, true, null);
		Film starWars6 = new Film("Star Wars 6", 3, "the picture is here", "Its cool", 10, 140, 12, false, null);
		Film starWars3 = new Film("Star Wars 3", 4, "the picture is here", "Its cool", 10, 140, 12, true, null);
		aktFilme.put(starWars.getId(),starWars);
		aktFilme.put(starWars2.getId(),starWars2);
		aktFilme.put(starWars6.getId(),starWars6);
		aktFilme.put(starWars3.getId(),starWars3);

		for(int i=0; i <= aktFilme.size(); i++) {
			if(aktFilme.get(i) != null){
				if(!aktFilme.get(i).getAktiv()) {
					deaktFilme.put(i, aktFilme.get(i));
					aktFilme.remove(i);
				}
			}
		}
	}
	public Map<Integer, Film> getAktFilme() {
		return aktFilme;
	}
	public void setAktFilme(Map<Integer, Film> neuAkt) {
		aktFilme = neuAkt;
	}
	public Map<Integer, Film> getDeaktFilme() {
		return deaktFilme;
	}
	public void setDeaktFilme(Map<Integer, Film> neuDeakt) {
		deaktFilme = neuDeakt;
	}

	public static void main(String[] args) {
		//SpringApplication.run(HelloWorldApplication.class, args);
		SpringApplication app =new SpringApplication(Application.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		app.run(args);

	}
}
