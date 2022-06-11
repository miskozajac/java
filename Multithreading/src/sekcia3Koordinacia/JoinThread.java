package sekcia3Koordinacia;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinThread {

    public static void main(String[] args) throws InterruptedException {
        List<Long> list = Arrays.asList(310000L,3452L,25L,12500L,8000L,90L);

        //pre vsetky longy vytvorim vlakna
        List<Faktorial> vlakna = new ArrayList<>();

        for (long l : list) {
            vlakna.add(new Faktorial(l));
        }

        for (Thread v : vlakna) {
            v.setDaemon(true);
            v.start();
            v.join(2000);
        }

        //v tomto bode beži hlavne vlakno spoločne s faktorial vlaknami
        //výpis
        for (int i = 0; i < vlakna.size(); i++) {
            Faktorial f = vlakna.get(i);
            //Thread.sleep(10);
            if (f.isFinished()) {
                System.out.println("Faktorial pre cislo "+f.input+" je "+f.getResult());
            } else {
                System.out.println("System ešte beží");
            }
        }
    }

    private static class Faktorial extends Thread {
        private long input;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public Faktorial(long input) {
            this.input = input;
        }

        @Override
        public void run() {
            this.result = factorial(input);
            this.isFinished = true;
        }

        private BigInteger factorial(long n) {
            BigInteger res = BigInteger.ONE;

            for (long l = n; l > 0; l--) {
                res = res.multiply(new BigInteger(Long.toString(l)));
            }
            System.out.println("Z Fakt : vysledok je "+res);
            return res;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }
}
