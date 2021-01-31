package com.example.lib;

import com.example.lib.Enum.Genre;
import com.example.lib.Repositories.*;
import com.example.lib.security.WebSecurityConfiguration;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*")
@SpringBootApplication
@RestController
@Import({WebSecurityConfiguration.class})
public class HelloWorldApplication {
    @Autowired
    public SitzRepository sitzRepository;

    @Autowired
    public SnackRepository snackRepository;

    @Autowired
    public GetraenkRepository getraenkRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    WarenkorbRepository warenkorbRepository;

    @Autowired
    VorstellungRepository vorstellungRepository;

    @Autowired
    BestellungRepository bestellungRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    KinosaalRepository kinosaalRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<Benutzer> userOptional = benutzerRepository.findByUsername(username);
                Benutzer user;
                if (userOptional.isEmpty()) {
                    throw new UsernameNotFoundException(username);
                }
                user = userOptional.get();
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswortHash(), Collections.emptyList());
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @RequestMapping(value = "/reset", produces = "application/json")
    public ResponseEntity<Object> home() throws ParseException {
        ticketRepository.deleteAll();

        sitzRepository.deleteAll();
        warenkorbRepository.deleteAll();
        vorstellungRepository.deleteAll();
        bestellungRepository.deleteAll();
        filmRepository.deleteAll();
        kinosaalRepository.deleteAll();
        benutzerRepository.deleteAll();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = sdf.parse("2020-12-26 15:30:00.000");
        Date date2 = sdf.parse("2020-12-26 20:30:00.000");
        Date date3 = sdf.parse("2020-12-26 21:30:00.000");

        Genre genre = Genre.SCI_FI;

        Vorstellung testVor = new Vorstellung(date1, new BigDecimal(8), true);
        Vorstellung testVor2 = new Vorstellung(date2, new BigDecimal(9), true);
        Vorstellung testVor3 = new Vorstellung(date3, new BigDecimal(9), true);
        Film filmT = new Film("Star Wars", "Bild", "Das ist ein neuer Film", 9, 140, 12, true, genre);
        Film filmT2 = new Film("Harry Potter", "Bild", "Das ist ein noch neuerer Film", 8, 150, 12, true, genre);
        Kinosaal saalT = new Kinosaal(50, 5, 10);

        filmRepository.save(filmT);
        filmRepository.save(filmT2);
        kinosaalRepository.save(saalT);
        for (int i = 1; i < 3; i++) {
            for (int k = 1; k < 3; k++) {
                Sitz sitz = new Sitz(i, k, false, new BigDecimal(1));
                sitz.setKinosaal(saalT);
                sitzRepository.save(sitz);
            }
        }
        testVor.setFilm(filmT);
        testVor.setSaal(saalT);
        testVor2.setFilm(filmT);
        testVor2.setSaal(saalT);
        testVor3.setFilm(filmT2);
        testVor3.setSaal(saalT);
        vorstellungRepository.save(testVor);
        vorstellungRepository.save(testVor2);
        vorstellungRepository.save(testVor3);


        Iterable<Kinosaal> alleSaeale = kinosaalRepository.findAll();
        for (Kinosaal saal : alleSaeale) {
            Sitz[] sitze = sitzRepository.findByKinosaal(saal);
            for (Sitz sitz : sitze) {
                if (saal.getMeineSitze() == null) {
                    saal.setMeineSitze();
                }
                saal.getMeineSitze().add(sitz);
            }
        }

        Ticket testT = new Ticket();
        Benutzer testBenutzer = new Benutzer();
        Warenkorb testWarenkorb = new Warenkorb();
        testBenutzer.setVorname("Max");
        testBenutzer.setNachname("Mustermann");
        testBenutzer.setUsername("Mustermann_Max");
        testBenutzer.setAlter(25);
        testBenutzer.setEmail("max.mustermann@gmail.com");
        testBenutzer.setPasswortHash("KFIWN");
        testBenutzer.setWarenkorb(testWarenkorb);
        testWarenkorb.setBenutzer(testBenutzer);
        Bestellung testBestellung = new Bestellung();
        ticketRepository.save(testT);
        bestellungRepository.save(testBestellung);
        benutzerRepository.save(testBenutzer);


        testBestellung.setBenutzer(testBenutzer);
        testT.setVorstellung(testVor);
        testT.setSitz(testVor.getSaal().getMeineSitze().get(0));
        testT.setGast(testBenutzer);
        testT.setKaeufer(testBenutzer);
        testT.setBezahlt(true);
        testT.setIstValide(false);
        testT.setBestellung(testBestellung);
        testT.setBezahlt(false);

        ticketRepository.deleteById(testT.getId());
        ticketRepository.save(testT);

        return new ResponseEntity<>("Die Datenbank wurde erfolgreich resettet!", HttpStatus.OK);
    }

    //TODO In den Controller verschieben
    @RequestMapping(value = "/ticket/bestellung", produces = "application/json", method = POST)
    public ResponseEntity<Object> setBestellung(@RequestBody(required = true) int[] ticketIds,
                                                Principal principal) {
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(principal.getName());
        if (optionalBenutzer.isPresent()) {
            Benutzer b = optionalBenutzer.get();
            if (b.getWarenkorb() == null) {
                Warenkorb w = new Warenkorb();
                b.setWarenkorb(w);
                benutzerRepository.save(b);
                warenkorbRepository.save(w);
            }
            /*for (Ticket t: tickets) {
                Optional<Ticket> optionalT = ticketRepository.findById(t.getId());
                if (optionalT.isPresent()) {
                    Ticket ticket = optionalT.get();
                    ticket.setBezahlt(false);
                }
            }*/
            Bestellung bestellung = b.getWarenkorb().reservieren(ticketIds, ticketRepository);
            for (int ticketId : ticketIds) {
                String s = sendEmail("kg42_kg42", b.getEmail(), ticketId);
            }
            bestellung.setBenutzer(b);
            bestellungRepository.save(bestellung);

            return new ResponseEntity<>(bestellung, HttpStatus.OK);
        }
        return new ResponseEntity<>("OO", HttpStatus.OK);
    }

    //TODO in den Controller verschieben
    @RequestMapping(value = "/bazahlen/bestellung/{bestellung_id}/iban/{iban_nummer}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<Object> payBestellung(@PathVariable(value = "bestellung_id") int bestellung_id, @PathVariable(value = "iban_nummer") String iban) {

        // TODO checken ob man die eigene Bestellung bestellt oder die Id evtl. falsch ist

        Optional<Bestellung> oB = bestellungRepository.findById(bestellung_id);
        String response = "";
        if (ibanValidation(iban)) {
            if (oB.isPresent()) {
                Bestellung bestellung = oB.get();
                bestellung.reservierungBezahlen(ticketRepository);
                bestellungRepository.save(bestellung);
                for (Ticket ticket : bestellung.getTicket()) {
                    response += sendEmail("kg42_kg42", bestellung.getBenutzer().getEmail(), ticket.getId());
                }
            } else {
                response = "Keine Bestellung gefunden";
            }
        } else {
            response = "Ungültige Iban";
        }

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    private boolean ibanValidation(String iban) {
        char[] ibanChar = iban.toCharArray();
        if (ibanChar[0] == "D".toCharArray()[0] && ibanChar[1] == "E".toCharArray()[0]) {
            return iban.length() >= 20;
        }
        return false;
    }

    private int sicherheitsschluesselGenerieren(String text) {
        int length = text.length();
        text += length;
        return text.hashCode();
    }

    public boolean validerTicketText(String text) {
        String[] textParts = text.split(">>>>>");
        if (textParts.length == 2) {
            String erwarteterSchluessel = "" + sicherheitsschluesselGenerieren(textParts[0]);
            return erwarteterSchluessel.equals(textParts[1]);
        } else {
            return false;
        }
    }

    // TODO für static damit die Methoden in die Controller verschoben werden können
    public String sendEmail(String password, String to, int ticketId) {

        Optional<Ticket> oTicket = ticketRepository.findById(ticketId);
        Optional<Film> oFilm;
        String qrText = "Fehler beim Ticket erzeugen";
        //Ticket ticket;
        if (oTicket != null) {
            Ticket ticket = oTicket.get();
            int filmId = ticket.getVorstellung().getFilm().getId();
            oFilm = filmRepository.findById(filmId);

            String gastPreiskategorie = ticket.getGast().getPreiskategorie().toString();
            int saal_id = (ticket.getVorstellung().getSaal() != null) ? ticket.getVorstellung().getSaal().getId() : 5;
            Date zeit = ticket.getVorstellung().getStartZeit();
            int sitzReihe = (ticket.getSitz() != null) ? ticket.getSitz().getReihe() : 2;
            int sitzSpalte = (ticket.getSitz() != null) ? ticket.getSitz().getSpalte() : 2;
            Boolean bezahlt = ticket.isBezahlt();
            double preis = ticket.getPreis();
            String filmName = (oFilm != null) ? oFilm.get().getName() : "filmname";
            qrText = "KINO-TICKET\n\nFilm: " + filmName + "\nSaal: " + saal_id + "\nReihe: " + sitzReihe
                    + "\nSpalte: " + sitzSpalte + "\nUhrzeit: " + zeit + "\nPreisklasse: "
                    + gastPreiskategorie + "\nPreis: " + preis + "\nBezahlt: " + (bezahlt ? "Ja" : "Nein");
        } else {
            return "Ticket wurde nicht gefunden. Es wurde keine E-Mail verschickt";
        }
        qrText += "\nSicherheitsschlüssel: ";
        int sicherheitsschluessel = sicherheitsschluesselGenerieren(qrText);
        qrText += ">>>>>" + sicherheitsschluessel;
        BufferedImage img = null;
        try {
            img = generateQRCodeImage(qrText);
        } catch (Exception e) {
            System.out.println("ERROR: QR-Code imgage generation threw Exception. Stack Trace as follows: \n\n");
            e.printStackTrace();
            System.out.println("\n\n End of Stack Trace \n\n");
        }

        String from = "kreative.gruppe42@gmail.com";
        String sub = "Ihre Kinotickets";
        String msg = "Sehr geehrte Kundin / sehr geehrter Kunde \n\nwir wünschen Ihnen viel Spaß in der Vorstellung. Anbei finden sie einen QR Code, welcher Ihre Eintrittskarte zum Film darstellt. Sollten sie mehrere Tickets bestellt haben, werden diese in seperaten Emails versendet. \n\nWir freuen uns auf Ihren Besuch, \nIhr Kreative Gruppe 42 Team";

        // Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        // get Session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        // compose message

        Message message = new MimeMessage(session);
        Multipart multiPart = new MimeMultipart("alternative");

        try {

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(msg, "utf-8");
            MimeBodyPart att = new MimeBodyPart();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] imageBytes = new byte[0];
            try {

                ImageIO.write(img, "png", baos);
                baos.flush();
                imageBytes = baos.toByteArray();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayDataSource bds = new ByteArrayDataSource(imageBytes, "image/png");
            att.setDataHandler(new DataHandler(bds));
            att.setFileName("./kinotickets.png");
            att.setHeader("Content-ID", "<image>");

            multiPart.addBodyPart(textPart);
            multiPart.addBodyPart(att);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(sub);
            message.setContent(multiPart);

            // send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "Email wurde an " + to + " gesendet";
    }

    // return new ResponseEntity<>("Email soll an " + to + " gesendet werden", HttpStatus.OK);
    @RequestMapping(value = "/test/sendEmail/{pw}/ticket/{ticket_id}/empfaengeradresse/{adresse}", produces = "application/json")
    public ResponseEntity<Object> sendEmailRequest(@PathVariable(value = "adresse") String to,
                                                   @PathVariable(value = "pw") String password, @PathVariable(value = "ticket_id") int ticketId) {
        return new ResponseEntity<Object>(sendEmail(password, to, ticketId), HttpStatus.OK);
    }

    private static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private static Optional<Benutzer> getCurrentUser(Principal principal, BenutzerRepository benutzerRepository) {
        return benutzerRepository.findByUsername(principal.getName());
    }

    public static void main(String[] args) {
        //SpringApplication.run(HelloWorldApplication.class, args);
        SpringApplication app = new SpringApplication(HelloWorldApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);
    }
}
