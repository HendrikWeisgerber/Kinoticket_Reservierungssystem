import java.util.concurrent.Semaphore;

import java.util.concurrent.Semaphore;

public class Aufgabenteil1 extends Thread {
    Semaphore mutex = new Semaphore(1);
    Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(1);
    int Buffer = 0;


    public Aufgabenteil1 () {
    }
    public void enterLok0() {

//überprüfen ob Puffer aktuell gleich 0
        try{
            if(Buffer != 0) {
                System.out.println(" Die Lok0 wartet auf Lok1");
                full.acquire();
            }mutex.acquire();
            System.out.println("Die Lok 0 fährt in das mittlere Teilstück");
            Thread.sleep((long) (Math.random()*200));
        }catch(Exception e) {e.printStackTrace();}

    }
    public void exitLok0() {
//
        Buffer = 1;
        System.out.println(" Die Lok 0 verlässt das Mittelstück");

        empty.release();

        mutex.release();
    }
    public void enterLok1() {
//überprüfen ob Puffer aktuell gleich 0
        try {
            if(Buffer != 1) {
                System.out.println(" Die Lok1 muss warten auf Lok0");
                empty.acquire();
            }mutex.acquire();
            System.out.println("Die Lok 1 fährt in das mittlere Teilstück");
            Thread.sleep((long) (Math.random()*200));
        } catch (InterruptedException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void exitLok1() {

//
        Buffer = 0;
        System.out.println(" Die Lok 1 verlässt das Mittelstück");
        full.release();
        mutex.release();
    }
    public static void main(String[] args) {
        Aufgabenteil1 Aufg = new Aufgabenteil1();
        new Thread(new Lok0(Aufg)).start();
        new Thread(new Lok1(Aufg)).start();

    }
}