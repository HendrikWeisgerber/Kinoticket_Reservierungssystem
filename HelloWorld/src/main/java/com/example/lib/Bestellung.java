package com.example.lib;

public class Bestellung {
private Ticket[] ticket;
private Benutzer benutzer;
private boolean bezahlt;
//private Zahlungsmethode zahlungsmethode;

public Bestellung(Ticket[] ticket, Benutzer benutzer, boolean bezahlt) { //,Zahlungsmethode zahlungsmethode
    this.ticket = ticket;
    this.benutzer = benutzer;
    this.bezahlt=bezahlt;
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
    this.bezahlt= true; //TODO bezahl möglichkeiten müssen implementiert werden
}
public void rechnungSenden(){
    System.out.println(this.benutzer.getEmail()); //TODO implementieren der echten email versendung
}

public boolean isBezahlt() {
	return bezahlt;
}

public void setBezahlt(boolean bezahlt) {
	this.bezahlt = bezahlt;
}

}
