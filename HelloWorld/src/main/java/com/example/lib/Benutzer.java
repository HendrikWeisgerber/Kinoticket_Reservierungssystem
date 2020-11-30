package com.example.lib;

public class Benutzer {
    private String vorname;
    private String nachname;
    private String username;
    private int id;
    private int alter;
    private String email;
    private String passwortHash;
    private Warenkorb warenkorb;
    private Film[] wunschliste;
    private Boolean newsletter;
    //private Rechte rechte;
    //private Preiskategorie preiskategorie;
    //private Zahlungsmethode zahlungsmethode;
    //private Zone lieblingszone;



    public Benutzer(String vorname, String nachname, String username, int id, int alter, String email,
            String passwortHash, Warenkorb warenkorb,Film[] wunschliste, Boolean newsletter) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.id = id;
        this.alter = alter;
        this.email = email;
        this.passwortHash = passwortHash;
        this.warenkorb = warenkorb;
        this.wunschliste= wunschliste;
        this.newsletter = newsletter;
    }
    public Benutzer(String vorname, String nachname, String username, int id, int alter, String email,
    String passwortHash, Warenkorb warenkorb, Boolean newsletter) {
    this.vorname = vorname;
    this.nachname = nachname;
    this.username = username;
    this.id = id;
    this.alter = alter;
    this.email = email;
    this.passwortHash = passwortHash;
    this.warenkorb = warenkorb;
    this.newsletter = newsletter;
}


    
    public String getVorname() {
        return this.vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return this.nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlter() {
        return this.alter;
    }

    public void setAlter(int alter) {
        this.alter = alter;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswortHash() {
        return this.passwortHash;
    }

    public void setPasswortHash(String passwortHash) {
        this.passwortHash = passwortHash;
    }

    public Warenkorb getWarenkorb() {
        return this.warenkorb;
    }

    public void setWarenkorb(Warenkorb warenkorb) {
        this.warenkorb = warenkorb;
    }

    public Film[] getWunschliste() {
        return this.wunschliste;
    }

    public void setWunschliste(Film[] wunschliste) {
       this.wunschliste = wunschliste;
    }

    public void derWunschlisteHinzufuegen(Film film){
        Film[] neueWunschListe = new Film[this.wunschliste.length+1];
        int i =0;
        for (Film alterWunschFilm: this.wunschliste){
            neueWunschListe[i] =alterWunschFilm;
            i++;
        }
        neueWunschListe[i]=film;
    }

    public Boolean getNewsletter() {
        return this.newsletter;
    }

    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }
//TODO Preiskategorien bestimmen und Rechte 







}
