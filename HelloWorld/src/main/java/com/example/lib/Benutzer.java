package com.example.lib;

import com.example.lib.Enum.Preiskategorie;
import com.example.lib.Enum.Rechte;
import com.example.lib.Enum.Zone;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Benutzer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String vorname;
    private String nachname;
    private String username;
    @Column(name = "age")
    private int alter;
    private String email;
    private String passwortHash;
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warenkorb_id", referencedColumnName = "id")
    private Warenkorb warenkorb;
    @Transient
    private Film[] wunschliste;
    private Boolean newsletter;
    private Rechte rechte;
    private Preiskategorie preiskategorie;
    private Zone lieblingszone;

    // private Zahlungsmethode zahlungsmethode;

    public Benutzer(String vorname, String nachname, String username, int id, int alter, String email,
            String passwortHash, Warenkorb warenkorb, Film[] wunschliste, Boolean newsletter) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.id = id;
        this.alter = alter;
        this.email = email;
        this.passwortHash = passwortHash;
        this.warenkorb = warenkorb;
        this.wunschliste = wunschliste;
        this.newsletter = newsletter;
        this.preiskategorie = Preiskategorie.ERWACHSENER;
        this.rechte = Rechte.USER;
        this.lieblingszone = Zone.MITTE_MITTE;
    }

    public Benutzer(String vorname, String nachname, String username, int id, int alter, String email,
            String passwortHash, Warenkorb warenkorb, Film[] wunschliste, Boolean newsletter,
            Preiskategorie preiskategorie) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.id = id;
        this.alter = alter;
        this.email = email;
        this.passwortHash = passwortHash;
        this.warenkorb = warenkorb;
        this.wunschliste = wunschliste;
        this.newsletter = newsletter;
        this.preiskategorie = preiskategorie;
        this.rechte = Rechte.USER;
        this.lieblingszone = Zone.MITTE_MITTE;

    }

    public Benutzer(String vorname, String nachname, String username, int id, int alter, String email,
            String passwortHash, Warenkorb warenkorb, Film[] wunschliste, Boolean newsletter,
            Preiskategorie preiskategorie, Rechte rechte) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.id = id;
        this.alter = alter;
        this.email = email;
        this.passwortHash = passwortHash;
        this.warenkorb = warenkorb;
        this.wunschliste = wunschliste;
        this.newsletter = newsletter;
        this.preiskategorie = preiskategorie;
        this.rechte = rechte;
        this.lieblingszone = Zone.MITTE_MITTE;
    }

    public Benutzer(String vorname, String nachname, String username, int id, int alter, String email,
            String passwortHash, Warenkorb warenkorb, Film[] wunschliste, Boolean newsletter,
            Preiskategorie preiskategorie, Rechte rechte, Zone zone) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.id = id;
        this.alter = alter;
        this.email = email;
        this.passwortHash = passwortHash;
        this.warenkorb = warenkorb;
        this.wunschliste = wunschliste;
        this.newsletter = newsletter;
        this.preiskategorie = preiskategorie;
        this.rechte = rechte;
        this.lieblingszone = zone;
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
        this.wunschliste = new Film[0];
        this.preiskategorie = Preiskategorie.ERWACHSENER;
        this.rechte = Rechte.USER;
        this.lieblingszone = Zone.MITTE_MITTE;
    }

    @Autowired
    public Benutzer() {

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

    public void derWunschlisteHinzufuegen(Film film) {
        Film[] neueWunschListe = new Film[this.wunschliste.length + 1];
        int i = 0;
        for (Film alterWunschFilm : this.wunschliste) {
            neueWunschListe[i] = alterWunschFilm;
            i++;
        }
        neueWunschListe[i] = film;
        this.wunschliste = neueWunschListe;
    }

    public Boolean getNewsletter() {
        return this.newsletter;
    }

    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    public Preiskategorie getPreiskategorie() {
        if (this.preiskategorie != null) {

            return this.preiskategorie;
        } else {
            return Preiskategorie.ERWACHSENER;
        }
    }

    public void setPreiskategorie(Preiskategorie preiskategorie) {
        this.preiskategorie = preiskategorie;
    }

    public Rechte getRechte() {
        return this.rechte;
    }

    public void setRechte(Rechte rechte) {
        this.rechte = rechte;
    }

    public Zone getLieblingszone() {
        return this.lieblingszone;
    }

    public void setLieblingszone(Zone lieblingszone) {
        this.lieblingszone = lieblingszone;
    }

    public BigDecimal preisschluesselBerechnen() {
        switch (this.preiskategorie) {
            case STUDIEREND:
                return new BigDecimal(0.8);
            case KIND:
                return new BigDecimal(0.6);
            case SENIOR:
                return new BigDecimal(0.7);
            case MENSCH_MIT_BEHINDERUNG:
                return new BigDecimal(0.5);
            case BEGLEITPERSON:
                return new BigDecimal(0);
            default:
                return new BigDecimal(1.0);
        }
    }

    public boolean istRichtigesPasswort(String passwort) {
        return ((Integer) passwort.hashCode()).toString().equals(passwortHash);
    }
    // TODO Rechte

}
