package com.example.lib;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Entity
public class Kinosaal {
    //private int anzahlSitze;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int spalte;
    private int reihe;
    private String name;
    @JsonManagedReference
    @Transient
    private ArrayList<Sitz> meineSitze;
    @Transient
    private ArrayList<Film> meineFilme;
    @Transient
    private ArrayList<Vorstellung> meineVorstellungen;
    
    @Autowired
    public Kinosaal(int spalte, int reihe){
        this.spalte = spalte;
        this.reihe = reihe;
    }

    @Autowired
    public Kinosaal(int anzahlSitze, int spalte, int reihe) {
        this.spalte = spalte;
        this.reihe = reihe;
    }

    @Autowired
    public Kinosaal(int spalte, int reihe, String name) {
        this.spalte = spalte;
        this.reihe = reihe;
        this.name = name;
    }


    @Autowired
    public Kinosaal() {
        
    }

    public int getAnzahlSitze() {
        return spalte * reihe;
    }

    /*public void setAnzahlSitze(int anzahlSitze) {
        this.anzahlSitze = anzahlSitze;
    }*/

    public int getSpalte() {
        return spalte;
    }

    public void setSpalte(int spalte) {
        this.spalte = spalte;
    }

    public int getReihe() {
        return reihe;
    }

    public void setReihe(int reihe) {
        this.reihe = reihe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Sitz> getMeineSitze() {
        return meineSitze;
    }

    public void setMeineSitze() {
        this.meineSitze = new ArrayList<Sitz>();
    }

    public ArrayList<Film> getMeineFilme() {
        return meineFilme;
    }

    public void setMeineFilme(ArrayList<Film> meineFilme) {
        this.meineFilme = meineFilme;
    }

    public ArrayList<Vorstellung> getMeineVorstellungen() {
        return meineVorstellungen;
    }

    public void setMeineVorstellungen(ArrayList<Vorstellung> meineVorstellungen) {
        this.meineVorstellungen = meineVorstellungen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float zuschauerStundenAuslastungBerechnen(SimpleDateFormat startZeit, SimpleDateFormat endZeit) {
        //TODO
        //startZeit.getCalendar().getTime()
        return 0;
    }
}
