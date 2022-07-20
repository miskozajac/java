package sekcia5dataShering;

public class Main5DataShering {
    public static void main(String[] args) throws InterruptedException {
        Inventar iv = new Inventar();

        InvPridajVlakno ip = new InvPridajVlakno(iv);
        InvUberVlakno iu = new InvUberVlakno(iv);

        ip.start();


        iu.start();

        ip.join();
        iu.join();

        System.out.println(iv.getItems());
    }

    private static class InvPridajVlakno extends Thread {
        private Inventar iv;
        public InvPridajVlakno(Inventar iv) {
            this.iv = iv;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                iv.pridaj();
            }
        }
    }

    private static class InvUberVlakno extends Thread {
        private Inventar iv;
        public InvUberVlakno(Inventar iv) {
            this.iv = iv;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                iv.uber();
            }
        }
    }

    private static class Inventar {
        private int items = 0;

        public void pridaj() {
            items++;
        }

        public void uber() {
            items--;
        }

        public int getItems() {
            return this.items;
        }
    }
}
