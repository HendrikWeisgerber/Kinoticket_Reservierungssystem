package com.example.lib;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "sitz_id", referencedColumnName = "id")
    private Sitz sitz;
    private double preis;
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
    @ManyToOne
    @JoinColumn(name="bestellung_id", referencedColumnName = "id")
    private Bestellung bestellung;
    private Date kaufdatum;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "snack_id", referencedColumnName = "id")
    private Snack snack;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "getraenk_id", referencedColumnName = "id")
    private Getraenk getraenk;


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

    public Bestellung getBestellung(){
        return this.bestellung;
    }

    public void setBestellung(Bestellung bestellung){
        this.bestellung = bestellung;
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

    public Date getKaufdatum() {
        return this.kaufdatum;
    }

    public void setKaufdatum(Date kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    
    public double getPreis() {
        this.updatePreis();
        return this.preis;
    }

    public Snack getSnack() {
        return this.snack;
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }
    public Getraenk getGetraenk() {
        return this.getraenk;
    }

    public void setGetraenk(Getraenk getraenk) {
        this.getraenk = getraenk;
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
            boolean istValide, Date kaufdatum) {
        this.id = id;
        this.sitz = sitz;
        this.vorstellung = vorstellung;
        this.gast = gast;
        //this.kaeufer = kaeufer;
        this.bezahlt = bezahlt;
        this.istValide = istValide;
        this.kaufdatum = kaufdatum;
        this.updatePreis();
    }
    public Ticket(Sitz sitz, Vorstellung vorstellung, Benutzer gast, Benutzer kaeufer, boolean bezahlt,
                  boolean istValide, Date kaufdatum) {
        this.sitz = sitz;
        this.vorstellung = vorstellung;
        this.gast = gast;
        //this.kaeufer = kaeufer;
        this.bezahlt = bezahlt;
        this.istValide = istValide;
        this.kaufdatum = kaufdatum;
        this.updatePreis();
    }

    public void updatePreis(){
        BigDecimal neuerPreis = new BigDecimal(0.0);
        if(this.vorstellung!= null && this.vorstellung.getGrundpreis() != null){
            neuerPreis = neuerPreis.add(this.vorstellung.getGrundpreis());
        }
        if(this.gast != null){
            neuerPreis = neuerPreis.multiply(this.gast.preisschluesselBerechnen());
        }
        if (this.snack != null) {

            BigDecimal snackPreis = new BigDecimal(0.0);

            switch (this.snack.getName()) {
                case POPCORN_SALZIG:
                    snackPreis = new BigDecimal(2.0);
                    break;
                case POPCORN_SUESS:
                    snackPreis = new BigDecimal(2.0);
                    break;
                case NACHOS:
                    snackPreis = new BigDecimal(4.0);
                    break;
                case GUMMIBAERCHEN:
                    snackPreis = new BigDecimal(1.5);
                    break;
            }
            switch (this.snack.getGroesse()) {
                case GROSS:
                    snackPreis = snackPreis.multiply(new BigDecimal(1.5));
                    break;
                case NORMAL:
                    snackPreis = snackPreis.multiply(new BigDecimal(1.0));
                    break;
                case KLEIN:
                    snackPreis = snackPreis.multiply(new BigDecimal(0.8));
                    break;
            }
        }

        if (this.getraenk != null) {

            BigDecimal getraenkPreis = new BigDecimal(0.0);

            switch (this.getraenk.getName()) {
                case COLA:
                    getraenkPreis = new BigDecimal(2.0);
                    break;
                case FANTA:
                    getraenkPreis = new BigDecimal(2.0);
                    break;
                case SPRITE:
                    getraenkPreis = new BigDecimal(2.0);
                    break;
                case SPEZI:
                    getraenkPreis = new BigDecimal(2.0);
                    break;
                case APFELSCHORLE:
                    getraenkPreis = new BigDecimal(1.8);
                    break;
                case WASSER:
                    getraenkPreis = new BigDecimal(1.0);
                    break;
                case PILS:
                    getraenkPreis = new BigDecimal(2.5);
                    break;
            }

            switch (this.snack.getGroesse()) {
                case GROSS:
                    getraenkPreis = getraenkPreis.multiply(new BigDecimal(1.5));
                    break;
                case NORMAL:
                    getraenkPreis = getraenkPreis.multiply(new BigDecimal(1.0));
                    break;
                case KLEIN:
                    getraenkPreis = getraenkPreis.multiply(new BigDecimal(0.8));
                    break;
            }
        }
        neuerPreis.setScale(2, RoundingMode.HALF_UP);
        this.preis = neuerPreis.doubleValue();
    }

}
