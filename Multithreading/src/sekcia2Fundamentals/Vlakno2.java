package sekcia2Fundamentals;

public class Vlakno2 {
    public static void main(String[] args) {
        Thread vlakno = new NewVlakno();

        vlakno.setName("Nove vlakno");
        vlakno.setPriority(Thread.MAX_PRIORITY);

        System.out.println("Sme vo vlakne: "+Thread.currentThread().getName()+" PRED začatim noveho vlakna");
        vlakno.start();
        System.out.println("Sme vo vlakne: "+Thread.currentThread().getName()+" PO začati noveho vlakna");
    }

    public static class NewVlakno extends Thread {
        @Override
        public void run() {
            //vyhoda je že tu nemusim volat staticke Thread.current... ale stači this
            System.out.println("Sme v NOVOM vlakne: "+this.getName()+" s prioritou: "+
                    Thread.currentThread().getPriority());
        }
    }
}
