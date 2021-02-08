package com.example.lib;

import javax.persistence.*;

import com.example.lib.Enum.EssenSorte;
import com.example.lib.Enum.GetraenkeSorte;
import com.example.lib.Enum.Groesse;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Entity
public class Getraenk {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private GetraenkeSorte name;
    private Groesse groesse;
    @ManyToMany
    private Set<Ticket> tickets;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GetraenkeSorte getName() {
        return this.name;
    }

    public void setName(GetraenkeSorte name) {
        this.name = name;
    }

    public Groesse getGroesse() {
        return this.groesse;
    }

    public void setGroesse(Groesse groesse) {
        this.groesse = groesse;
    }
    @Autowired
    public Getraenk(){
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Getraenk(GetraenkeSorte name, Groesse groesse){
        this.name = name;
        this.groesse = groesse;
    }

}