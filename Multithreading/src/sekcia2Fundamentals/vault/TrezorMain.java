package sekcia2Fundamentals.vault;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrezorMain {

    private static int MAX_VALUE = 9999;
    public static void main(String[] args) {
        Random r = new Random();
        Trezor trezor = new Trezor(r.nextInt(MAX_VALUE));
        List<Thread> vlakna = new ArrayList<>();

        vlakna.add(new AscendingHacker(trezor));
        vlakna.add(new DescendingHacker(trezor));
        vlakna.add(new Policajt());

        for (Thread vlakno : vlakna) {
            vlakno.start();
        }
    }

    private static class Trezor {
        private int heslo;

        public Trezor (int heslo) {
            this.heslo = heslo;
        }

        //ak tu nedám aspon tých 5 milisekund tak je heslo uhadnute hned
        public boolean isHesloSpravne (int tip) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return this.heslo == tip;
        }
    }

    private abstract static class HackerVlakno extends Thread {
        protected Trezor trezor;

        public HackerVlakno (Trezor t) {
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
            this.trezor = t;
        }

        @Override
        public synchronized void start() {
            System.out.println("Zacinam vlakno "+this.getName());
            super.start();
        }
    }

    private static class AscendingHacker extends HackerVlakno {
        public AscendingHacker(Trezor t) {
            super(t);
        }

        @Override
        public void run() {
            for (int i = 0; i <= MAX_VALUE; i++) {
                if (this.trezor.isHesloSpravne(i)) {
                    System.out.println(String.format("%s uhádol naše heslo %s a vykradol nás",this.getName(),i));
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHacker extends HackerVlakno {
        public DescendingHacker(Trezor t) {
            super(t);
        }

        @Override
        public void run() {
            for (int i = MAX_VALUE; i >= 0; i--) {
                if (this.trezor.isHesloSpravne(i)) {
                    System.out.println(String.format("%s uhádol naše heslo %s a vykradol nás",this.getName(),i));
                    System.exit(0);
                }
            }
        }
    }

    private static class Policajt extends Thread {
        @Override
        public void run() {
            for (int i = 10; i >= 1; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(i);
            }
            System.out.println("Chytili sme hackerov");
            System.exit(0);
        }
    }
}
