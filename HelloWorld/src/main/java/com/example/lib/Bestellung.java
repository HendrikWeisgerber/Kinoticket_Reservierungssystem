package com.example.lib;

public class Bestellung {
private Ticket[] ticket;
private Benutzer benutzer;
//private Zahlungsmethode zahlungsmethode;

public Bestellung(Ticket[] ticket, Benutzer benutzer) { //,Zahlungsmethode zahlungsmethode
    this.ticket = ticket;
    this.benutzer = benutzer;
    //this.zahlungsmethode = zahlungsmethode;
}

public Ticket[] getTicket() {
    return ticket;
}

public void setTicket(Ticket[] ticket) {
    this.ticket = ticket;
}

public Benutzer getBenutzer() {
    return benutzer;
}

public void setBenutzer(Benutzer benutzer) {
    this.benutzer = benutzer;
}

//public Zahlungsmethode getZahlungsmethode() {
//    return zahlungsmethode;
//}

//public void setZahlungsmethode(Zahlungsmethode zahlungsmethode) {
//    this.zahlungsmethode = zahlungsmethode;
//}


public void reservierungStornieren(){
    ticket=null;
}

public void reservierungBezahlen(){

}
public void rechnungSenden(){
    
}

}
