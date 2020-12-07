package com.example.lib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String bild;
    private String beschreibung;
    private int bewertung;
    private int laenge;
    private int mindestAlter;
    private boolean aktiv;
//TODO Genre enum statt string
    private String genre;
    @Transient
    private Vorstellung[] vorstellung;


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBild() {
        return this.bild;
    }

    public void setBild(String bild) {
        this.bild = bild;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getBewertung() {
        return this.bewertung;
    }

    public void setBewertung(int bewertung) {
        this.bewertung = bewertung;
    }

    public int getLaenge() {
        return this.laenge;
    }

    public void setLaenge(int laenge) {
        this.laenge = laenge;
    }

    public int getMindestAlter() {
        return this.mindestAlter;
    }

    public void setMindestAlter(int mindestAlter) {
        this.mindestAlter = mindestAlter;
    }

    public boolean getAktiv() {
        return this.aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public Vorstellung[] getVorstellung() {
        return this.vorstellung;
    }

    public String getGenre() {
        return this.genre;
    }

    @Autowired
    public Film(String name, String bild, String beschreibung, int bewertung, int laenge, int mindestAlter,
            boolean aktiv, String genre) {
        this.name = name;
        this.bild = bild;
        this.beschreibung = beschreibung;
        this.bewertung = bewertung;
        this.laenge = laenge;
        this.mindestAlter = mindestAlter;
        this.aktiv = aktiv;
        this.genre = genre;
    }

    @Autowired
    public Film() {
    }

    public void zuWunschlisteHinzufuegen(Benutzer benutzer) {
        //TODO
    }
    
    public ArrayList<SimpleDateFormat> zeigeVorstellungszeiten() {
        ArrayList<SimpleDateFormat> vorstellungszeiten = new ArrayList<SimpleDateFormat>();
        for(int i=0; i < this.getVorstellung().length; i++) {
            vorstellungszeiten.add(this.getVorstellung()[i].getStartZeit());
        }
        return vorstellungszeiten;
    }
    
    public void filmDeaktivieren() {
        this.aktiv = false;
    }
}
