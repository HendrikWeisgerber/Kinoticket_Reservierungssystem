package com.example.lib;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "sitz_id")
    private Sitz sitz;
    @ManyToOne
    @JoinColumn(name = "vorstellung_id")
    private Vorstellung vorstellung;
    @ManyToOne
    @JoinColumn(name="gast_id", referencedColumnName = "id")
    private Benutzer gast;
    @ManyToOne
    @JoinColumn(name="kaeufer_id")
    private Benutzer kaeufer;
    private boolean istValide;
    private SimpleDateFormat kaufdatum;

    //TODO: implementiere Essen und trinken funktionen

    @Autowired
    public Ticket() {
    }

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Sitz getSitz() {
        return this.sitz;
    }
    
    public void setSitz(Sitz sitz) {
        this.sitz = sitz;
    }
    
    public Vorstellung getVorstellung() {
        return this.vorstellung;
    }

    public void setVorstellung(Vorstellung vorstellung) {
        this.vorstellung = vorstellung;
    };
    
    public Benutzer getGast() {
        return this.gast;
    }
    
    public void setGast(Benutzer gast) {
        this.gast = gast;
    }

    public Benutzer getKaeufer() {
        //return this.kaeufer;
        return new Benutzer();
    }

    public void setKaeufer(Benutzer kaeufer) {
        //this.kaeufer = kaeufer;
    }

	public boolean getIstValide() {
		return this.istValide;
	}

	public void setIstValide(boolean istValide) {
		this.istValide = istValide;
	}

    public SimpleDateFormat getKaufdatum() {
        return this.kaufdatum;
    }

    public void setKaufdatum(SimpleDateFormat kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    public void inDenWarenkorb(){
        //TODO rethink this this.kaeufer.getWarenkorb().getTicket().append(this);
    }
    //TODO Preisberechnungen anpassen und aktivieren
    /*
    public BigDecimal preisBerechnen(){
        return (this.vorstellung.getGrundPreis() * this.gast.getPreiskategorie()
    }

    public void preiskategorieGastAnpassen(Preiskategorie neuePreiskategorie){
        this.gast.setPreiskategorie(neuePreiskategorie);
    }
    */
    public void ticketLoeschen(){
        this.istValide = false;
    }

    public Ticket(int id, Sitz sitz, Vorstellung vorstellung, Benutzer gast, Benutzer kaeufer, boolean bezahlt,
            boolean istValide, SimpleDateFormat kaufdatum) {
        this.id = id;
        this.sitz = sitz;
        this.vorstellung = vorstellung;
        this.gast = gast;
        //this.kaeufer = kaeufer;
        this.bezahlt = bezahlt;
        this.istValide = istValide;
        this.kaufdatum = kaufdatum;
    }

}
