package com.example.lib;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ArrayList;

public class Film {
    private String name;
    private String bild;
    private String beschreibung;
    private int bewertung;
    private int laenge;
    private int mindestAlter;
    private boolean aktiv;

    private Genre[] genre;
    private Vorstellung[] vorstellung;

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

    public Genre[] getGenre() {
        return this.genre;
    }

    public Film(String name, String bild, String beschreibung, int bewertung, int laenge, int mindestAlter,
            boolean aktiv, Genre genre, Vorstellung[] vorstellung) {
        this.name = name;
        this.bild = bild;
        this.beschreibung = beschreibung;
        this.bewertung = bewertung;
        this.laenge = laenge;
        this.mindestAlter = mindestAlter;
        this.aktiv = aktiv;
        this.genre = genre;
        this.vorstellung = vorstellung;
    }

    public void zuWunschlisteHinzufuegen(Benutzer benutzer) {
        //TODO
    }
    
    public ArrayList<Date> zeigeVorstellungszeiten() {
        //TODO 
        //Datenbankabfrage nach allen Vorstellungen mit dem entsprechenden Film
        //Jeweils Anfangszetipunkt in Arrayliste abspeichern
        ArrayList<Date> vorstellungszeiten = new ArrayList<Date>();
        return vorstellungszeiten;
    }
    
    public void filmDeaktivieren() {
        this.aktiv = false;
    }
}
