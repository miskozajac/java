package cvicenia.gettersSettersTest.test;

import cvicenia.gettersSettersTest.api.ClothingProduct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ProductTest {

    public static void main(String[] args) {
        testGetters(ClothingProduct.class);
        testSetters(ClothingProduct.class);
    }

    private static List<Field> vsetkyFieldy(Class<?> klass) {
        if (klass == null || klass.equals(Object.class)) {
            return Collections.emptyList();
        }

        Field[] fieldyKlasy = klass.getDeclaredFields();

        List<Field> zdedeneFieldy = vsetkyFieldy(klass.getSuperclass());

        List<Field> vsetky = new ArrayList<>();
        vsetky.addAll(Arrays.asList(fieldyKlasy));
        vsetky.addAll(zdedeneFieldy);

        return vsetky;
    }

    public static void testSetters(Class<?> klas) {
        //aby sme mali aj fieldy rodičov toto prepíšeme
        //Field[] polia = klas.getDeclaredFields();

        List<Field> polia = vsetkyFieldy(klas);

        for (Field f : polia) {
            String setterName = "set"+capitalize(f.getName());

            //setter budeme hladať hned pomocou getMethod
            Method setter = null;
            try {
                setter = klas.getMethod(setterName,f.getType());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(String.format("Setter: %s nenajdeny",setterName));
            }

            //ak setter exituje tak este musi byť void
            if (!setter.getReturnType().equals(void.class)) {
                throw new IllegalStateException(String.format("Setter %s má návratový typ %s; musí byť void",setterName,setter.getReturnType().getName()));
            }
        }
    }

    public static void testGetters (Class<?> klas) {
        //aby sme mali aj fieldy rodičov toto prepíšeme
        //Field[] polia = klas.getDeclaredFields();

        List<Field> polia = vsetkyFieldy(klas);

        Map<String, Method> mapMetody = mapMethodNameToMethod(klas);

        for (Field f : polia) {
            String getterName = "get"+capitalize(f.getName());

            if (!mapMetody.containsKey(getterName)) {
                throw new IllegalStateException(String.format("Pole: %s nemá getter",f.getName()));
            }

            //ak pole má getter pozrieme dalsie vlastnosti
            Method getter = mapMetody.get(getterName);

            if (!getter.getReturnType().equals(f.getType())) {
                throw new IllegalStateException(
                        String.format("Getter: %s má nesprávny návratový typ %s; očakáva sa %s",getterName
                                ,getter.getReturnType().getTypeName(),f.getType().getTypeName()));
            }

            //getter nesmie mať parametre
            if (getter.getParameterCount() > 0) {
                throw new IllegalStateException(String.format("Getter: %s nesmie mať argumenty ale tento má %d",getterName,getter.getParameterCount()));
            }
        }
    }

    private static String capitalize(String name) {
        return name.substring(0,1).toUpperCase().concat(name.substring(1));
    }

    private static Map<String, Method> mapMethodNameToMethod(Class<?> klas) {
        //zaujimaju ma len gettre a tie musia byť public takže volam len getMethods
        Method[] metody = klas.getMethods();

        Map<String, Method> nameToMethod = new HashMap<>();

        for (Method m : metody) {
            if (m.getName().startsWith("get")) {
                nameToMethod.put(m.getName(),m);
            }
        }

        return nameToMethod;
    }

}
