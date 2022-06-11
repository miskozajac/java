package sekcia3Koordinacia;

public class Test31 {
    public static void main(String[] args) {

        Thread v = new Thread(new BlockVlakno());

        v.start();
        v.interrupt();
    }

    private static class BlockVlakno implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                System.out.println("Ukoncenie po hodeni interupt");
            }
        }
    }
}
