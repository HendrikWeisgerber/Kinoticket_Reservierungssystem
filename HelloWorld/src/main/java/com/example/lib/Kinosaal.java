package com.example.lib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Kinosaal {
    private int anzahlSitze;
    private int anzahlSpalte;
    private int anzahlReihe;
    private int id;
    private ArrayList<Sitz> meineSitze;
    private ArrayList<Film> meineFilme;
    private ArrayList<Vorstellung> meineVorstellungen;

    public Kinosaal(int anzahlSitze, int anzahlSpalte, int anzahlReihe, int id, ArrayList<Sitz> meineSitze, ArrayList<Film> meineFilme, ArrayList<Vorstellung> meineVorstellungen) {
        this.anzahlSitze = anzahlSitze;
        this.anzahlSpalte = anzahlSpalte;
        this.anzahlReihe = anzahlReihe;
        this.id = id;
        this.meineSitze = meineSitze;
        this.meineFilme = meineFilme;
        this.meineVorstellungen = meineVorstellungen;
    }

    public int getAnzahlSitze() {
        return anzahlSitze;
    }

    public void setAnzahlSitze(int anzahlSitze) {
        this.anzahlSitze = anzahlSitze;
    }

    public int getAnzahlSpalte() {
        return anzahlSpalte;
    }

    public void setAnzahlSpalte(int anzahlSpalte) {
        this.anzahlSpalte = anzahlSpalte;
    }

    public int getAnzahlReihe() {
        return anzahlReihe;
    }

    public void setAnzahlReihe(int anzahlReihe) {
        this.anzahlReihe = anzahlReihe;
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

    public void setMeineSitze(ArrayList<Sitz> meineSitze) {
        this.meineSitze = meineSitze;
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

    public float zuschauerStundenAuslastungBerechnen(SimpleDateFormat startZeit, SimpleDateFormat endZeit) {
        //TODO
        //startZeit.getCalendar().getTime()
        return 0;
    }
}
