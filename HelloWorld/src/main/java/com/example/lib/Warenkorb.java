package com.example.lib;

public class Warenkorb {
    private Benutzer benutzer;
    private Ticket[] ticket;

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

public void bezahlen(){

}

public Bestellung bezahlen(Ticket[] ticket){

    return new Bestellung(ticket, benutzer);
}

public void reservieren(){

}

public Bestellung reservieren(Ticket[] ticket){

    return new Bestellung (ticket, benutzer);
}

}
