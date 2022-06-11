package sekcia3Koordinacia;

import java.math.BigInteger;

public class Daemon {
    public static void main(String[] args) {

        Thread v = new Thread(new VelkyVypocet(new BigInteger("200000"),new BigInteger("1000000")));
        v.setDaemon(true);
        v.start();
        v.interrupt();
    }

    private static class VelkyVypocet implements Runnable {

        private BigInteger base;
        private BigInteger power;

        public VelkyVypocet(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base+"*"+power+" = "+pow(base,power));
        }

        private BigInteger pow(BigInteger c, BigInteger p) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(p) != 0; i=i.add(BigInteger.ONE)) {
                result = result.multiply(c);
            }
            return result;
        }
    }
}
