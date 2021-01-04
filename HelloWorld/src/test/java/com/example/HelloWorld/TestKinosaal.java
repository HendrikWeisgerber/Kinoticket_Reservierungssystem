package com.example.HelloWorld;

import com.example.lib.Kinosaal;

import org.junit.jupiter.api.*;

public class TestKinosaal {
    
    static private Kinosaal kinosaal;
    static private int reihe, spalte;

    @BeforeAll
    static void setUpAll(){
        reihe = (int)(Math.random() * 99 + 1);
        spalte = (int)(Math.random() * 99 + 1);
        kinosaal = new Kinosaal(623, reihe, spalte);
    }

    @BeforeEach
    void setUpEach(){
        reihe = (int)(Math.random() * 99 + 1);
        spalte = (int)(Math.random() * 99 + 1);
        kinosaal.setReihe(reihe);
        kinosaal.setSpalte(spalte);
    }

    @Test
    void testAnzahlSitze(){
        Assertions.assertEquals(spalte * reihe, kinosaal.getAnzahlSitze());
    }

    @Test
    void testAnzahlSitzeFürNullSpalte(){
        spalte = 0;
        kinosaal.setSpalte(spalte);
        Assertions.assertEquals(0, kinosaal.getAnzahlSitze());
    }

    @Test
    void testAnzahlSitzeFürNullReihe(){
        reihe = 0;
        kinosaal.setReihe(reihe);
        Assertions.assertEquals(0, kinosaal.getAnzahlSitze());
    }



    

}
