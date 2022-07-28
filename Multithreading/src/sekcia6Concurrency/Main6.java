package sekcia6Concurrency;

import java.util.Random;

public class Main6 {

    public static void main(String[] args) {
        Metrics m = new Metrics();

        BiznisLogic b1 = new BiznisLogic(m);
        BiznisLogic b2 = new BiznisLogic(m);
        MetricsPrinter mp = new MetricsPrinter(m);

        b1.start();
        b2.start();
        mp.start();
    }

    public static class MetricsPrinter extends Thread {
        private Metrics metric;

        public MetricsPrinter(Metrics m) {
            this.metric = m;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                double aktPriemer = metric.getPriemer();

                System.out.println("Aktpriem je: "+aktPriemer);
            }
        }
    }

    public static class BiznisLogic extends Thread {
        private Metrics metric;
        private Random random = new Random();

        public BiznisLogic(Metrics m) {
            this.metric = m;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long end = System.currentTimeMillis();

                metric.addSample(end-start);
            }
        }
    }

    public static class Metrics {
        private long count = 0;
        private volatile double priemer = 0.0;

        public synchronized void addSample(long sample) {
            double currentSum = priemer * count;
            count++;
            priemer = (currentSum + sample) / count;
        }

        public double getPriemer() {
            return priemer;
        }
    }
}
