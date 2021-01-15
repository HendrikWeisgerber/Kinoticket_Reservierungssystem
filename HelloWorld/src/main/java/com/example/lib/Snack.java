package com.example.lib;

import java.math.BigDecimal;

import javax.persistence.*;

import com.example.lib.Enum.EssenSorte;
import com.example.lib.Enum.Groesse;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Snack {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private EssenSorte name;
    private Groesse groesse;

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

    public Snack(EssenSorte name, Groesse groesse){
        this.name = name;
        this.groesse = groesse;
    }

}