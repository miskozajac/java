public class Main {
    public static void main(String[] args) {
        vypisFb(40);
    }

    static int a = 0;
    static int b = 1;

    private static void vypisFb(int h) {
        int v = 0;
        for (int i = 0; i <= h; i++) {
            if (i == 1) {
                v = i;
            } else if (i != 0) {
                a=b;
                b=v;
                v = a+b;
            }
            System.out.println(v);
        }
    }
}
