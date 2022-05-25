package cvicenia.fieldsJson;

import cvicenia.fieldsJson.data.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class MainFieldsJson {

    public static void main(String[] args) throws IllegalAccessException {
        Address a = new Address("hlavna",(short)5);
        Person p = new Person("Misko",true,33,1000,a,new Company("tuna","Bratiška",a));

        Actor h = new Actor("Misko Zajac", new String[] {"super film","mega film"});
        Actor actor1 = new Actor("Elijah Wood", new String[]{"Lord of the Rings", "The Good Son"});
        Actor actor2 = new Actor("Ian McKellen", new String[]{"X-Men", "Hobbit"});
        Actor actor3 = new Actor("Orlando Bloom", new String[]{"Pirates of the Caribbean", "Kingdom of Heaven"});

        Movie movie = new Movie("Lord of the Rings", 8.8f, new String[]{"Action", "Adventure", "Drama"},
                new Actor[]{actor1, actor2, actor3});

        String j = objectToJson(movie,0);
        System.out.println(j);
    }

    //metody pre json writer

    private static String objectToJson(Object o, int tabSize) throws IllegalAccessException {
        Field[] fs = o.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append(tab(tabSize));
        sb.append("{");
        sb.append("\n");

        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);

            //ak je field synthetic znamena že je generovany javou (pomôcka napr pri inner klasach)
            if (f.isSynthetic()) {
                continue;
            }
            sb.append(tab(tabSize+1));
            sb.append(formatStringValue(f.getName()));

            sb.append(":");

            if (f.getType().isPrimitive()) {
                //už nepošlem cely field ale jeho hodnotu (co môze byť objekt) a typ fieldu
                sb.append(formatPrimiteValue(f.get(o),f.getType()));
            } else if (f.getType().equals(String.class) || f.getType().equals(Boolean.class)) {
                if (f.get(o) != null) {
                    sb.append(formatStringValue(f.get(o).toString()));
                }
            } else if (f.getType().isArray()) {
                sb.append(arrayToJson(f.get(o),tabSize+1));
            } else {
                sb.append(objectToJson(f.get(o),tabSize+1));
            }

            if (i != fs.length-1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        //vrátenie tabu dozadu kvôli uzavretiu
        sb.append(tab(tabSize));
        sb.append("}");

        return sb.toString();
    }

    private static String arrayToJson(Object o, int tabSize) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        int dlzka = Array.getLength(o);

        Class<?> typ = o.getClass().getComponentType();

        sb.append("[");
        sb.append("\n");
        for (int i = 0; i < dlzka; i++) {
            Object b = Array.get(o,i);

            if (typ.isPrimitive()) {
                //klasa má fieldy ale pole nie, preto prerobíme metodu formatPrimiveValue aby prijimala objekt a rodiča
                //po refaktore môžem poslať do formatPrimitiveValue hodnotu elementu a jeho typ
                sb.append(tab(tabSize+1));
                sb.append(formatPrimiteValue(b,typ));
            } else if (typ.equals(String.class)) {
                sb.append(tab(tabSize+1));
                sb.append(formatStringValue(b.toString()));
            } else {
                //ak typ nieje ani String ani primitivny typ tak je to iný objekt
                sb.append(objectToJson(b,tabSize+1));
            }

            if (i != dlzka-1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(tab(tabSize));
        sb.append("]");

        return sb.toString();
    }

    //metoda prida uvodzovky k string hodnote aby bola validna pre json
    //použije sa aj pre nazov fieldu a aj pre hodnotu fieldu
    private static String formatStringValue(String s) {
        return String.format("\"%s\"",s);
    }

    //metoda preformatuje primitivne typy do jsonu
    private static String formatPrimiteValue(Object e, Class<?> typ) throws IllegalAccessException {
        if (typ.equals(boolean.class)
                || typ.equals(int.class)
                || typ.equals(long.class)
                || typ.equals(short.class)) {
            return e.toString();
        } else if (typ.equals(double.class) || typ.equals(float.class)) {
            return String.format("%.02f",e);
        }
        throw new RuntimeException(String.format("Neni podpory pre %s",typ.getName()));
    }
    //pred refaktoringom:
    /*
    //najskôr som prechadzal cez klasu takze mal som fieldy, v metode ale priamo ich netreba, stači mi len typ fieldu
    //hodnotu som vral pomocou rodiča, metoda sa dá prerobiť tak že pošlem do nej object a jeho typ
    private static String formatPrimiteValue(Field f, Object parent) throws IllegalAccessException {
        if (f.getType().equals(boolean.class)
            || f.getType().equals(int.class)
            || f.getType().equals(long.class)
            || f.getType().equals(short.class)) {
            return f.get(parent).toString();
        } else if (f.getType().equals(double.class) || f.getType().equals(float.class)) {
            return String.format("%.02f",f.get(parent));
        }
        throw new RuntimeException(String.format("Neni podpory pre %s",f.getType().getName()));
    }
    */

    private static String tab(int tabSize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tabSize; i++) {
            //pridanie tabulatora
            sb.append("\t");
        }
        return sb.toString();
    }
}
