package com.example.lib;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class Vorstellung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date startZeit;
    private BigDecimal grundpreis;
    private boolean aktiv;
    @ManyToOne
    @JoinColumn(name = "kinosaal_id")
    private Kinosaal saal;
    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;
    @Transient
    private Ticket[] ticket;
    
    @Autowired
    public Vorstellung() {

    }

    public Vorstellung(int id, Film film, BigDecimal grundpreis, boolean aktiv) {
        this.setId(id);
        this.setFilm(film);
        this.setGrundpreis(grundpreis);
        this.setAktiv(aktiv);
    }

    public Kinosaal getSaal() {
		return saal;
	}

	public void setSaal(Kinosaal saal) {
		this.saal = saal;
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

    public Date getStartZeit() {
        return startZeit;
    }

    public void setStartZeit(Date startZeit) {
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
