package com.example.lib;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Vorstellung {
    private int id;
    private Date startZeit;
    private BigDecimal grundpreis;
    private boolean aktiv;

    private Kinosaal saal;
    private Film film;
    private Ticket[] ticket;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public Vorstellung(int id, Date startZeit, Film film, BigDecimal grundpreis, boolean aktiv) {
        this.setId(id);
        this.setStartZeit(startZeit);
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

    public Date getEndZeit() {
        Date endZeit;
        endZeit = new Date(this.getStartZeit().getTime() + film.getLaenge()*60000);
        return endZeit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void sitzplanAnzeigen() {
        //TODO
        // Über eine Datenbankabfrage an die besetzten Sitze kommen und diese als Array mit dem entsprechenden Platz (Reihe,Spalte) ausgeben
    }
    public Ticket sitzWaehlen(int reihe, int spalte) {
        //TODO
        //1. Prüfen ob Sitz belegt
        //      Datenbankabfrage nach allen Tickets für diese Vorstellung --> entsprechende Sitze des Tickets sind belegt
        //2. Wer findet den Sitz zu der erntsprechenden Reihe und Spalte?; welches Datum ist Kaufdatum?
        ArrayList<Sitz> ausSitze = this.getSaal().getMeineSitze();
        //null nur temporäre Lösung, später lieber mit Exception ersetzen
        Sitz ausSitz = null;
        for(int i = 0; i < ausSitze.size(); i++) {
            
            if(ausSitze.get(i).getReihe() == reihe && ausSitze.get(i).getSpalte() == spalte) {
                ausSitz = ausSitze.get(i);
            }
        }
        return new Ticket(0, ausSitz, null, null, null); 
    }
    public float prozentsatzFreierSitze() {
        //TODO
        //1. Prüfen welche Sitz belegt
        //      Datenbankabfrage nach allen Tickets für diese Vorstellung --> entsprechende Sitze der Tickets sind belegt
        ArrayList<Sitz> ausSitze = this.getSaal().getMeineSitze();
        int anzahlSitze = ausSitze.size();
        //anzahlSitze mit anzahlBelegteTickets teilen
        return 0;
    }
}
