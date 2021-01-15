package com.example.HelloWorld;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.lib.Benutzer;
import com.example.lib.Film;
import com.example.lib.Sitz;
import com.example.lib.Ticket;
import com.example.lib.Vorstellung;
import com.example.lib.Warenkorb;
import com.example.lib.Enum.Preiskategorie;
import com.example.lib.Enum.Rechte;
import com.example.lib.Enum.Zone;

import org.junit.jupiter.api.*;

public class TestTicket {

    static private Ticket ticket;

    @BeforeAll
    static void setUpAll(){
        Sitz sitz1 = new Sitz(2, 4, false, new BigDecimal(10.0));
        Benutzer benutzer = new Benutzer();
        Warenkorb warenkorb = new Warenkorb(benutzer, new Ticket[0]);

        benutzer = new Benutzer("Kurt C.", "Hose", "KurtCeHose", 1, 23, "kurtCHose@gmail.com", "456", warenkorb, new Film[0], 
                                false, Preiskategorie.ERWACHSENER, Rechte.USER, Zone.MITTE_MITTE);
        warenkorb.setBenutzer(benutzer);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Vorstellung vorstellung = new Vorstellung(new Date(2021, 1, 22), new BigDecimal(10.0), true);

        ticket = new Ticket(123, sitz1, vorstellung, benutzer, benutzer, true, true, sdf);
    }

    @Test
    void testTicketPreisStandard(){
        ticket.updatePreis();
        Assertions.assertEquals(10.0, ticket.getPreis());
    }

    @Test
    void testTicketPreisStudent(){
        ticket.getGast().setPreiskategorie(Preiskategorie.STUDIEREND);
        ticket.updatePreis();
        Assertions.assertEquals(8.0, ticket.getPreis());
    }
    
}
