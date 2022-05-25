package cvicenia;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class Main {

    public static void main (String[] args) throws ClassNotFoundException {
        Class<String> stringClass = String.class;
        Map<String,Integer> mapa = new HashMap<>();
        Class<?> mapClass = mapa.getClass();
        Class<?> stvorecKlasa = Class.forName("cvicenia.Main$Stvorec");

        Object kruh = new Drawable() {
            @Override
            public int getPocetRohov() {
                return 0;
            }
        };

        //printClassInfo(stringClass,mapClass,stvorecKlasa);
        printClassInfo(Collection.class,boolean.class, Color.class, Farba.class, int[][].class, kruh.getClass());
    }

    //volitelny pocet parametrov typu class
    private static void printClassInfo(Class<?> ... klasy) {
        for (Class<?> klas : klasy) {
            System.out.println(String.format("Class name: %s und class package name: %s"
                    ,klas.getSimpleName()
                    ,klas.getPackageName()));
            Class<?>[] interfaciKlasy = klas.getInterfaces();

            for (Class<?> intf : interfaciKlasy) {
                System.out.println(String.format("Class %s implementuje %s"
                        ,klas.getSimpleName()
                        ,intf.getSimpleName()));
            }
            System.out.println("Is array: "+klas.isArray());
            System.out.println("Is primitive: "+klas.isPrimitive());
            System.out.println("Is enum: "+klas.isEnum());
            System.out.println("Is interface: "+klas.isInterface());
            System.out.println("Is anonymna: "+klas.isAnonymousClass());
            System.out.println();
        }
    }

    private static interface Drawable {
        int getPocetRohov();
    }

    private static enum Farba {
        MODRA,ZELENA,CERVENA
    }
    private static class Stvorec implements Drawable {

        public int getPocetRohov() {
            return 4;
        }
    }
}
