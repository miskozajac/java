package sekcia2Fundamentals;

public class Vlakno1 {
    public static void main(String[] args) {
        Thread vlakno = new Thread(new Runnable() {
            @Override
            public void run() {
                //toto sa vypiše ako posledne lebo schedulnutie vlakna chvilu trva, OS to rieši
                //10 je najvyššia priorita, tu je to jedno, aj tak je vykonana až po hlavnej ale v appke
                //s viacerymi vlaknami to má efekt
                System.out.println("Sme v NOVOM vlakne: "+Thread.currentThread().getName()+" s prioritou: "+
                        Thread.currentThread().getPriority());
            }
        });
        vlakno.setName("Nove vlakno");
        vlakno.setPriority(Thread.MAX_PRIORITY);

        System.out.println("Sme vo vlakne: "+Thread.currentThread().getName()+" PRED začatim noveho vlakna");
        vlakno.start();
        System.out.println("Sme vo vlakne: "+Thread.currentThread().getName()+" PO začati noveho vlakna");
    }
}
