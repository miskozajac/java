package cvicenia.modifiersAukcia;

import cvicenia.modifiersAukcia.auction.Auction;
import cvicenia.modifiersAukcia.auction.Bid;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MainAukcia {

    public static void main(String[] args) throws ClassNotFoundException {
       //runAukciu();
        //private BidImpl sa dá zavolať len cez Class.forName
       //printClassModifiers(Class.forName("cvicenia.modifiersAukcia.auction.Bid$Builder$BidImpl"));
        //printClassModifiers(Serializable.class);
        //printMethodModifiers(Auction.class.getDeclaredMethods());
        printFieldModifiers(Auction.class.getDeclaredFields());
    }

    public static void printFieldModifiers(Field[] atr) {
        for (Field f : atr) {
            int mod = f.getModifiers();
            System.out.println(String.format("Access modifier for field %s is %s",f.getName(),getModifierName(mod)));

            if (Modifier.isVolatile(mod)) {
                System.out.println("Field je volatie");
            }

            if (Modifier.isFinal(mod)) {
                System.out.println("Field je final");
            }

            if (Modifier.isTransient(mod)) {
                System.out.println("Field je transient");
            }
        }
    }

    public static void printMethodModifiers(Method[] metody) {
        for (Method m : metody) {
            int mod = m.getModifiers();
            System.out.println(String.format("Access modifier of method %s is %s",m.getName(),getModifierName(mod)));

            if (Modifier.isSynchronized(mod)) {
                System.out.println("Metoda je synchronized");
            } else {
                System.out.println("Metoda NIE je synchronized");
            }
            System.out.println();
        }
    }

    public static void printClassModifiers(Class<?> klas) {
        int modifier = klas.getModifiers();
        System.out.println(String.format("Access modifier of class %s is %s",klas.getSimpleName(),getModifierName(modifier)));

        if (Modifier.isAbstract(modifier)) {
            System.out.println("Klasa je abstractna");
        }

        if (Modifier.isInterface(modifier)) {
            System.out.println("Klasa je interface");
        }

        if (Modifier.isStatic(modifier)) {
            System.out.println("Klasa je staticka");
        }
    }

    //každy modifiers má svoju byte reprezentaciu
    private static String getModifierName(int modifier) {
        if (Modifier.isPublic(modifier)) {
            return "public";
        } else if (Modifier.isPrivate(modifier)) {
            return "private";
        } else if (Modifier.isProtected(modifier)) {
            return "protected";
        } else {
            return "package-private";
        }
    }
    public static void runAukciu() {
        Auction aukcia = new Auction();
        aukcia.startAuction();

        Bid b1 = Bid.builder().setBidderName("Hrac1").setPrice(10).build();
        aukcia.addBid(b1);

        Bid b2 = Bid.builder().setBidderName("Hrac2").setPrice(15).build();
        aukcia.addBid(b2);

        aukcia.stopAuction();

        System.out.println(aukcia.getAllBids());
        System.out.println("Najviac: "+aukcia.getHighestBid().get());
    }
}
