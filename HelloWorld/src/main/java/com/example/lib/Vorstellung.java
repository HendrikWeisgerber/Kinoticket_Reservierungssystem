package com.example.lib;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vorstellung {
    private int id;
    private SimpleDateFormat startZeit;
    private BigDecimal grundpreis;
    private boolean aktiv;

    private Kinosaal saal;
    private Film film;
    private Ticket[] ticket;
    
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
