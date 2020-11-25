package com.example.lib;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Sitz {
    private int id;
    private int reihe;
    private int spalte;
    private boolean barriereFrei;
    private BigDecimal preisschluessel;
    private Kinosaal meinKinosaal;
    private ArrayList<Ticket> meineTickets;

    public Sitz(int id, int reihe, int spalte, boolean barriereFrei, BigDecimal preisschluessel, Kinosaal meinKinosaal, ArrayList<Ticket> meineTickets) {
        this.id = id;
        this.reihe = reihe;
        this.spalte = spalte;
        this.barriereFrei = barriereFrei;
        this.preisschluessel = preisschluessel;
        this.meinKinosaal = meinKinosaal;
        this.meineTickets = meineTickets;
    }

    public ArrayList<Ticket> getMeineTickets() {
        return meineTickets;
    }

    public void setMeineTickets(ArrayList<Ticket> meineTickets) {
        this.meineTickets = meineTickets;
    }

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

    public Kinosaal getMeinKinosaal() {
        return meinKinosaal;
    }

    public void setMeinKinosaal(Kinosaal meinKinosaal) {
        this.meinKinosaal = meinKinosaal;
    }
}
