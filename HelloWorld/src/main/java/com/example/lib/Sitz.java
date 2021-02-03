package com.example.lib;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Sitz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int reihe;
    private int spalte;
    private boolean barriereFrei;
    private BigDecimal preisschluessel;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "kinosaal_id", referencedColumnName = "id")
    private Kinosaal kinosaal;
    /*@ManyToOne
    @JoinColumn(name = "kinosaal_id")
    private Kinosaal meinKinosaal;*/
    //private ArrayList<Ticket> meineTickets;

    // @Autowired
    // public Sitz(int id, int reihe, int spalte, boolean barriereFrei, BigDecimal preisschluessel, Kinosaal meinKinosaal) {
    //     this.id = id;
    //     this.reihe = reihe;
    //     this.spalte = spalte;
    //     this.barriereFrei = barriereFrei;
    //     this.preisschluessel = preisschluessel;
    //     this.meinKinosaal = meinKinosaal;
    // }
    @Autowired
    public Sitz() {

    }

    public Sitz(int reihe, int spalte, boolean barriereFrei, BigDecimal preisschluessel) {
        this.reihe = reihe;
        this.spalte = spalte;
        this.barriereFrei = barriereFrei;
        this.preisschluessel = preisschluessel;
    }

    // public ArrayList<Ticket> getMeineTickets() {
    //     return meineTickets;
    // }

    // public void setMeineTickets(ArrayList<Ticket> meineTickets) {
    //     this.meineTickets = meineTickets;
    // }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReihe() {
        return reihe;
    }

    public void setReihe(int reihe) {
        this.reihe = reihe;
    }

    public int getSpalte() {
        return spalte;
    }

    public void setSpalte(int spalte) {
        this.spalte = spalte;
    }

    public boolean isBarriereFrei() {
        return barriereFrei;
    }

    public void setBarriereFrei(boolean barriereFrei) {
        this.barriereFrei = barriereFrei;
    }

    public BigDecimal getPreisschluessel() {
        return preisschluessel;
    }

    public void setPreisschluessel(BigDecimal preisschluessel) {
        this.preisschluessel = preisschluessel;
    }

    public Kinosaal getKinosaal() {
        return kinosaal;
    }

    public void setKinosaal(Kinosaal kinosaal) {
        this.kinosaal = kinosaal;
    }
}
