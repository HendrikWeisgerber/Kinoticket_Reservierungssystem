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
    @JoinColumn(name = "sitz_id", referencedColumnName = "id")
    private Sitz sitz;
    @ManyToOne
    @JoinColumn(name = "vorstellung_id", referencedColumnName = "id")
    private Vorstellung vorstellung;
    @ManyToOne
    @JoinColumn(name="gast_id", referencedColumnName = "id")
    private Benutzer gast;
    @ManyToOne
    @JoinColumn(name="kaeufer_id", referencedColumnName = "id")
    private Benutzer kaeufer;
    private boolean bezahlt;
    private boolean istValide;
    @ManyToOne
    @JoinColumn(name="warenkorb_id", referencedColumnName = "id")
    private Warenkorb warenkorb;
    private boolean istBestellt;
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

    public Warenkorb getWarenkorb(){
        return this.warenkorb;
    }

    public void setWarenkorb(Warenkorb warenkorb){
        this.warenkorb = warenkorb;
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
        return this.kaeufer;
    }

    public void setKaeufer(Benutzer kaeufer) {
        this.kaeufer = kaeufer;
    }
    
    public int getKaueferId(){
        return (kaeufer == null) ? null : kaeufer.getId();
    }
    
    public boolean isBezahlt() {
        return this.bezahlt;
    }

    public void setBezahlt(boolean bezahlt) {
        this.bezahlt = bezahlt;
    }

	public boolean getIstValide() {
		return this.istValide;
    }
    

	public void setIstValide(boolean istValide) {
		this.istValide = istValide;
    }
    
    
    public boolean getIstBestellt() {
		return this.istBestellt;
    }
    
    public void setIstBestellt(boolean istBestellt) {
		this.istBestellt = istBestellt;
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
