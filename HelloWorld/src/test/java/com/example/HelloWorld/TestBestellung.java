package com.example.HelloWorld;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.lib.Benutzer;
import com.example.lib.Bestellung;
import com.example.lib.Film;
import com.example.lib.Sitz;
import com.example.lib.Ticket;
import com.example.lib.Vorstellung;
import com.example.lib.Warenkorb;
import com.example.lib.Enum.Genre;

import org.junit.jupiter.api.*;

public class TestBestellung {

    static private Bestellung bestellung;
    
    @BeforeAll
    static void setUpAll(){
        Benutzer benutzer = new Benutzer();
        Warenkorb warenkorb = new Warenkorb(benutzer, new Ticket[0]);
        Film[] wunschliste = new Film[1];
        Genre genre = Genre.SCI_FI;
        Film film = new Film("Star Wars", "Bild", "Das ist ein neuer Film", 9, 140, 12, true, genre);
        wunschliste[0] = film;
        String passwort = ((Integer)(int)(Math.random() * 100000000)).toString();
        benutzer = new Benutzer("Kurt C.", "Hose", "KurtCeHose", 1, 14, "kurtCHose@gmail.com", ((Integer)passwort.hashCode()).toString(), warenkorb, wunschliste, true);
        benutzer.getWarenkorb().setBenutzer(benutzer);

        Vorstellung vorstellung = new Vorstellung();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        
        Sitz sitz1 = new Sitz(2, 4, false, new BigDecimal(10.0));
        Sitz sitz2 = new Sitz(2, 5, false, new BigDecimal(10.0));
        Sitz sitz3 = new Sitz(2, 6, false, new BigDecimal(10.0));
        
        Ticket ticket1 = new Ticket(1, sitz1, vorstellung, benutzer, benutzer, false, true, sdf);
        Ticket ticket2 = new Ticket(1, sitz2, vorstellung, benutzer, benutzer, false, true, sdf);
        Ticket ticket3 = new Ticket(1, sitz3, vorstellung, benutzer, benutzer, false, true, sdf);

        Ticket[] tickets = new Ticket[3];
        tickets[0] = ticket1;
        tickets[1] = ticket2;
        tickets[2] = ticket3;
        

        bestellung = new Bestellung(tickets, benutzer, false);
    }

    @BeforeEach
    void setUpEach(){
        Benutzer benutzer = new Benutzer();
        Warenkorb warenkorb = new Warenkorb(benutzer, new Ticket[0]);
        Film[] wunschliste = new Film[1];
        Genre genre = Genre.SCI_FI;
        Film film = new Film("Star Wars", "Bild", "Das ist ein neuer Film", 9, 140, 12, true, genre);
        wunschliste[0] = film;
        String passwort = ((Integer)(int)(Math.random() * 100000000)).toString();
        benutzer = new Benutzer("Kurt C.", "Hose", "KurtCeHose", 1, 14, "kurtCHose@gmail.com", ((Integer)passwort.hashCode()).toString(), warenkorb, wunschliste, true);
        benutzer.getWarenkorb().setBenutzer(benutzer);
        Vorstellung vorstellung = new Vorstellung();
        Sitz sitz1 = new Sitz(2, 4, false, new BigDecimal(10.0));
        Sitz sitz2 = new Sitz(2, 5, false, new BigDecimal(10.0));
        Sitz sitz3 = new Sitz(2, 6, false, new BigDecimal(10.0));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Ticket ticket1 = new Ticket(1, sitz1, vorstellung, benutzer, benutzer, false, true, sdf);
        Ticket ticket2 = new Ticket(1, sitz2, vorstellung, benutzer, benutzer, false, true, sdf);
        Ticket ticket3 = new Ticket(1, sitz3, vorstellung, benutzer, benutzer, false, true, sdf);
        bestellung.getTicket()[0] = ticket1;
        bestellung.getTicket()[1] = ticket2;
        bestellung.getTicket()[2] = ticket3;
        bestellung.setBenutzer(benutzer);
        //bestellung.setBezahlt(false);
    }

    @Test
    void testBestellungStornieren(){
        Ticket[] tickets = bestellung.getTicket();
        bestellung.reservierungStornieren();
        for(Ticket ticket : tickets){
            Assertions.assertEquals(false, ticket.getIstValide());
        }
    }
/*
    @Test
    void testBestellungBezahlen(){
        Ticket[] tickets = bestellung.getTicket();
        bestellung.reservierungBezahlen();
        for(Ticket ticket : tickets){
            Assertions.assertEquals(true, ticket.isBezahlt());
        }
    }*/
    
}
