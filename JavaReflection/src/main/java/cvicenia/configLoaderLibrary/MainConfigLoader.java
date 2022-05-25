package cvicenia.configLoaderLibrary;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Scanner;

public class MainConfigLoader {
    //cesta sa píše od projektu nie od tejto klasy
    private static final Path GAME_CONFIG_PATH = Path.of("src/main/resources/game-properties.cfg");
    private static final Path UI_CONFIG_PATH = Path.of("src/main/resources/user-interface.cfg");

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GameConfig gc = vytvorObjectZKonfigu(GameConfig.class,GAME_CONFIG_PATH);
        UserInterfaceConfig uic = vytvorObjectZKonfigu(UserInterfaceConfig.class,UI_CONFIG_PATH);

        System.out.println(uic);
    }

    //najdenie konstruktore, vytvorenie instancie pomocou neho, najdenie fieldov instancie a ich naplnenie
    //ignorujeme fieldy ktoré niesu v konfigu a aj tie ktore sú ineho typu def v metode parseValue
    private static <T> T vytvorObjectZKonfigu(Class<T> klas, Path path) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {

        //nacitanie obsahu configu
        Scanner s = new Scanner(path);

        //v tejto API očakavame klasu len s jednym konstruktorom bez atributov ale bez kons vtedy sa berie autogenerovaný
        //tu môže byť v <> aj ? aj T
        Constructor<T> c = klas.getDeclaredConstructor();
        c.setAccessible(true);

        //vytvorime objekt danej klasy
        T instancia = (T) c.newInstance();

        while (s.hasNextLine()) {
            String confRiadok = s.nextLine();

            String[] p = confRiadok.split("=");

            String propertyName = p[0];
            String propertyValue = p[1];

            Field field;

            try {
                field = klas.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.out.println(String.format("Nepodporovana property: %s",propertyName));
                continue;
            }

            field.setAccessible(true);

            //prehodenie propertyValue do správneho typu
            Object parseValue;

            if (field.getType().isArray()) {
                //kedze je to pole musim zavolat componentType
                parseValue = parseArray(field.getType().getComponentType(),propertyValue);
            } else {
                parseValue = parseValue(field.getType(),propertyValue);
            }

            field.set(instancia,parseValue);
        }

        return instancia;
    }

    private static Object parseValue(Class<?> typ, String propertyValue) {
        if (typ.equals(int.class)) {
            return Integer.parseInt(propertyValue);
        } else if (typ.equals(short.class)) {
            return Short.parseShort(propertyValue);
        } else if (typ.equals(long.class)) {
            return Long.parseLong(propertyValue);
        } else if (typ.equals(double.class)) {
            return Double.parseDouble(propertyValue);
        } else if (typ.equals(float.class)) {
            return Float.parseFloat(propertyValue);
        } else if (typ.equals(String.class)) {
            return propertyValue;
        }
        throw new RuntimeException(String.format("Nepodporovany typ: %s",typ.getName()));
    }

    private static Object parseArray(Class<?> typPola, String value) {
        String[] hodnoty = value.split(",");

        //funguje to len tak, ak vytvorim list a naplnim hodnotami tak to nevie setnut do fieldu, ani ked dám ten list.toArray
        Object pole = Array.newInstance(typPola,hodnoty.length);

        for (int i=0; i < hodnoty.length; i++) {
            Array.set(pole,i,parseValue(typPola,hodnoty[i]));
        }

        return pole;
    }
}
