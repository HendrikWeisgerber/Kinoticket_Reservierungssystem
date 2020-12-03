package com.example.lib;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Warenkorb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Benutzer benutzer;
    private Ticket[] ticket;

    @Autowired
    public Warenkorb() {

    }

    public Warenkorb(Benutzer benutzer, Ticket[] ticket) {
        this.benutzer = benutzer;
        this.ticket = ticket;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Ticket[] getTicket() {
        return ticket;
    }

    public void setTicket(Ticket[] ticket) {
        this.ticket = ticket;
    }

public Bestellung bezahlen(){
    Bestellung neueBestellung =new Bestellung(this.ticket, benutzer, true);
    this.ticket=null;
    return neueBestellung; 
}
//TODO bezahlen muss noch fertig implementiert werden

public Bestellung bezahlen(Ticket[] ausgewaehlteTickets){

    for (Ticket ausgewaehltesTicket: ausgewaehlteTickets){
        try{
           for(int i=0; i<this.ticket.length; i++){
                if (ausgewaehltesTicket==ticket[i]){
                    this.ticket[i]=null;
                    break;
                }
            }
            throw new Exception();
        }catch(Exception e){}//TODO die Exception ausarbeiten
    }
    return new Bestellung (ausgewaehlteTickets, benutzer, true);
}


public Bestellung reservieren(){
    Bestellung neueBestellung =new Bestellung(this.ticket, benutzer, false);
    this.ticket=null;
    return neueBestellung; 
}

public Bestellung reservieren(Ticket[] ausgewaehlteTickets){

    for (Ticket ausgewaehltesTicket: ausgewaehlteTickets){
        try{
           for(int i=0; i<this.ticket.length; i++){
                if (ausgewaehltesTicket==ticket[i]){
                    this.ticket[i]=null;
                    break;
                }
            }
            throw new Exception();
        }catch(Exception e){}//TODO die Exception ausarbeiten
    }
    return new Bestellung (ausgewaehlteTickets, benutzer, false);
}

}
