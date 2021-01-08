package com.example.HelloWorld;

import com.example.lib.Benutzer;
import com.example.lib.Film;
import com.example.lib.Ticket;
import com.example.lib.Warenkorb;
import com.example.lib.Enum.Genre;

import com.example.lib.Enum.Genre;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.*;

public class TestBenutzer {

    static private Benutzer benutzer;
    static private String passwort;

    @BeforeAll
    static void setUpAll(){
        Warenkorb warenkorb = new Warenkorb(benutzer, new Ticket[0]);
        Film[] wunschliste = new Film[1];
        Film film = new Film("Star Wars", "Bild", "Das ist ein neuer Film", 9, 140, 12, true, Genre.SCI_FI);
        wunschliste[0] = film;
        passwort = ((Integer)(int)(Math.random() * 100000000)).toString();
        benutzer = new Benutzer("Kurt C.", "Hose", "KurtCeHose", 1, 14, "kurtCHose@gmail.com", ((Integer)passwort.hashCode()).toString(), warenkorb, wunschliste, true);
        benutzer.getWarenkorb().setBenutzer(benutzer);
    }

    @Test
    void testDerWunschlisteHinzufuegen(){
        
        int alteLaenge = benutzer.getWunschliste().length;
        
        Film[] alteWunschliste = benutzer.getWunschliste();
        Film film = new Film("Harry Potter", "Bild", "Das ist ein noch neuerer Film", 8, 150, 12, true, Genre.FANTASY);
        benutzer.derWunschlisteHinzufuegen(film);

        Assertions.assertEquals((alteLaenge + 1) , benutzer.getWunschliste().length);
        Assertions.assertEquals(film, benutzer.getWunschliste()[benutzer.getWunschliste().length - 1]);
        int i = 0;
        for(Film alterFilm : alteWunschliste){
            Assertions.assertEquals(alterFilm, benutzer.getWunschliste()[i]);
            i++;
        }
    }

    @Test
    void testIstRichtigesPasswort(){
        Assertions.assertEquals(true, benutzer.istRichtigesPasswort(passwort));
    }

    @Test
    void negativTestIstRichtigesPasswort(){
        String falschesPasswort;
        for(int i = 0; i < 100; i++){
            do{

                falschesPasswort = ((Integer)(int)(Math.random() * 100000000)).toString();
            }while(falschesPasswort.equals(passwort));
            Assertions.assertEquals(false, benutzer.istRichtigesPasswort(falschesPasswort));
        }
    }
    
}
