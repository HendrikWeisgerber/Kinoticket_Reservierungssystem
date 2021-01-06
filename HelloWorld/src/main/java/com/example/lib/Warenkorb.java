package com.example.lib;

import com.example.lib.Repositories.TicketRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class Warenkorb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "benutzer_id", referencedColumnName = "id")
    private Benutzer benutzer;
    @Transient
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

    public Bestellung bezahlen() {
        Bestellung neueBestellung = new Bestellung(this.ticket, benutzer, true);
        this.ticket = null;
        return neueBestellung;
    }
//TODO bezahlen muss noch fertig implementiert werden

    public Bestellung bezahlen(Ticket[] ausgewaehlteTickets) {

        for (Ticket ausgewaehltesTicket : ausgewaehlteTickets) {
            try {
                for (int i = 0; i < this.ticket.length; i++) {
                    if (ausgewaehltesTicket == ticket[i]) {
                        this.ticket[i] = null;
                        break;
                    }
                }
                throw new Exception();
            } catch (Exception e) {
            }//TODO die Exception ausarbeiten
        }
        return new Bestellung(ausgewaehlteTickets, benutzer, true);
    }


    public Bestellung reservieren() {
        Bestellung neueBestellung = new Bestellung(this.ticket, benutzer, false);
        this.ticket = null;
        return neueBestellung;
    }

    public Bestellung reservieren(Ticket[] ausgewaehlteTickets) {

        for (Ticket ausgewaehltesTicket : ausgewaehlteTickets) {
            try {
                for (int i = 0; i < this.ticket.length; i++) {
                    if (ausgewaehltesTicket == ticket[i]) {
                        this.ticket[i] = null;
                        break;
                    }
                }
                throw new Exception();
            } catch (Exception e) {
            }//TODO die Exception ausarbeiten
        }
        return new Bestellung(ausgewaehlteTickets, benutzer, false);
    }

    //Vorteil, id gibt weniger Platz zum Manipiulieren
    public Bestellung reservieren(int[] ausgewaehlteTicketsId, TicketRepository ticketRepository) {
        Ticket[] tickets = new Ticket[ausgewaehlteTicketsId.length];
        for (int i = 0; i < ausgewaehlteTicketsId.length; i++) {
            Optional<Ticket> optionalT = ticketRepository.findById(ausgewaehlteTicketsId[i]);
            if (optionalT.isPresent()) {
                Ticket ticket = optionalT.get();
                tickets[i] = ticket;
            }
        }
        if(tickets[0]==null) {
            System.out.println("Keine Tickets gefunden");
        }
        return new Bestellung(tickets, benutzer, false);
    }

}
