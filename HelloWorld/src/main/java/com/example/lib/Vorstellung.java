package com.example.lib;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Vorstellung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private SimpleDateFormat startZeit;
    private BigDecimal grundpreis;
    private boolean aktiv;

    private Kinosaal saal;
    private Film film;
    private Ticket[] ticket;
    
    @Autowired
    public Vorstellung() {

    }

    public Vorstellung(int id, SimpleDateFormat startZeit, Film film, BigDecimal grundpreis, boolean aktiv) {
        this.setId(id);
        this.setStartZeit(startZeit);
        this.setFilm(film);
        this.setGrundpreis(grundpreis);
        this.setAktiv(aktiv);
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public BigDecimal getGrundpreis() {
        return grundpreis;
    }

    public void setGrundpreis(BigDecimal grundpreis) {
        this.grundpreis = grundpreis;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public SimpleDateFormat getStartZeit() {
        return startZeit;
    }

    public void setStartZeit(SimpleDateFormat startZeit) {
        this.startZeit = startZeit;
    }

    // public SimpleDateFormat getEndZeit() {
    //     SimpleDateFormat endZeit;
    //     endZeit = this.getStartZeit();
    //     Date date = endZeit;
    //     endZeit.getTime();
    // }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // public void sitzplanAnzeigen() {
    //     //TODO
    // }
    // public float prozentsatzFreierSitze() {
    //     //TODO
    // }
}
