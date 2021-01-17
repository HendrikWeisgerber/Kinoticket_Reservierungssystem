import java.util.concurrent.Semaphore;

public class AufgabeA extends Thread {
    private int size = 1;
    private int[] buffer;
    private int ctr = 0;
    private int nextfree = 0;
    private int nextfull = 0;
    private Semaphore mutex = new Semaphore(1, true);
    private Semaphore full = new Semaphore(0, true);
    private Semaphore empty;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //enterLok();

            // exitLok();
        }
    }

    public AufgabeA(int size) {
        this.size = size;
        buffer = new int[size];
        empty = new Semaphore(size, true);
    }

    public void enterLok0(int data) {
        try {
            System.out.println("Lok0 kommt an");
            empty.acquire();
            mutex.acquire();
            System.out.println("Lok0 fährt und schreibt" + data);
            buffer[nextfree] = data;
            nextfree = (nextfree+1)%size;
            ctr++;
            mutex.release();
            full.release();
            System.out.println("Lok0 verlässt");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int  enterLok1() {
        int data = 0;
        try {
            System.out.println("Lok1 kommt an");
            full.acquire();
            mutex.acquire();
            System.out.println("Lok1 fährt und verbraucht");
            data = buffer|nextfull|;
            nextfull = (nextfull+1) % size;
            ctr--;
            mutex.release();
            empty.release();
            System.out.println("Lok2 verlässt mit" + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AufgabeA zug = new AufgabeA(1);
        new Thread(new Lok0(zug)).start();
        new Thread(new Lok1(zug)).start();
    }

    class Lok0 implements Runnable {
        AufgabeA
    }
}
