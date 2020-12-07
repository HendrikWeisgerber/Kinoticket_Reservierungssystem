package com.example.lib;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Bestellung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Benutzer benutzer;
    private boolean bezahlt;
    //private Zahlungsmethode zahlungsmethode;
    @Transient
    private Ticket[] ticket;

    @Autowired
    public Bestellung(Ticket[] ticket, Benutzer benutzer, boolean bezahlt) { //,Zahlungsmethode zahlungsmethode
        this.ticket = ticket;
        this.benutzer = benutzer;
        this.bezahlt=bezahlt;
        //this.zahlungsmethode = zahlungsmethode;
    }

    @Autowired
    public Bestellung() {
        
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Ticket[] getTicket() {
        return ticket;
    }

    public void setTicket(Ticket[] ticket) {
        this.ticket = ticket;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    //public Zahlungsmethode getZahlungsmethode() {
    //    return zahlungsmethode;
    //}

    //public void setZahlungsmethode(Zahlungsmethode zahlungsmethode) {
    //    this.zahlungsmethode = zahlungsmethode;
    //}


    public void reservierungStornieren(){
        ticket=null;
    }

    public void reservierungBezahlen(){
        this.bezahlt= true; //TODO bezahl möglichkeiten müssen implementiert werden
    }
    public void rechnungSenden(){
        System.out.println(this.benutzer.getEmail()); //TODO implementieren der echten email versendung
    }

    public boolean isBezahlt() {
        return bezahlt;
    }

    public void setBezahlt(boolean bezahlt) {
        this.bezahlt = bezahlt;
    }

}
