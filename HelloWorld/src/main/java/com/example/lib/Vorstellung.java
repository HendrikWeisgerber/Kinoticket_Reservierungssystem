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
    private int filmId;
    private Ticket[] ticket;
    
    @Autowired
    public Vorstellung() {

    }

    public Vorstellung() {
        super();
    }

    public Vorstellung(int id, Date startZeit, int filmId, BigDecimal grundpreis, boolean aktiv) {
        this.setId(id);
        this.setStartZeit(startZeit);
        this.setFilmId(filmId);
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

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public Date getStartZeit() {
        return startZeit;
    }

    public void setStartZeit(Date startZeit) {
        this.startZeit = startZeit;
    }

    /*public Date getEndZeit() {
        Date endZeit;
        endZeit = new Date(this.getStartZeit().getTime() + film.getLaenge()*60000);
        return endZeit;
    }*/

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
