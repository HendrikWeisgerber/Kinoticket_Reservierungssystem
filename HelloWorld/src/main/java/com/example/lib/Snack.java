package com.example.lib;

import com.example.lib.Enum.EssenSorte;
import com.example.lib.Enum.Groesse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Snack {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private EssenSorte name;
    private Groesse groesse;
    @ManyToMany
    private Set<Ticket> tickets;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EssenSorte getName() {
        return this.name;
    }

    public void setName(EssenSorte name) {
        this.name = name;
    }

    public Groesse getGroesse() {
        return this.groesse;
    }

    public void setGroesse(Groesse groesse) {
        this.groesse = groesse;
    }
    @Autowired
    public Snack(){
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Snack(EssenSorte name, Groesse groesse){
        this.name = name;
        this.groesse = groesse;
    }

}