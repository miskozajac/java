package cvicenia.fieldsJson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainArrays {
    public static void main(String[] args) {
        int[] p = {1,2,8,6,9};
        int[][][] p3 = {{{1,2},{8,6}},{{3,4},{5,6}},{{7,7},{2,3}},{{6,8},{9,4}}};
        List<String> s = new ArrayList<>();
        inspectArrayObject(p3);
        System.out.println(Array.getLength(s.toArray()));

        //inspectArrayValues(p3);
        System.out.println(getArrayElement(p,-2).toString());
    }

    private static Object getArrayElement(Object array, int index) {
        Object o = null;
        if (index < 0) {
            int l = Array.getLength(array);
            o = Array.get(array,l+index);
        } else {
            o = Array.get(array,index);
        }

        return o;
    }

    private static void inspectArrayValues(Object o) {
        int d = Array.getLength(o);
        System.out.print("[");
        for (int i = 0; i < d; i++) {
            Object b = Array.get(o,i);

            if (b.getClass().isArray()) {
                inspectArrayValues(b);
            } else {
                System.out.print(b);
            }

            if (i != d-1) {
                System.out.print(" , ");
            }
        }
        System.out.print("]");
    }

    private static void inspectArrayObject(Object o) {
        Class<?> k = o.getClass();
        System.out.println(String.format("Je pole: %s",k.isArray()));

        Class<?> aK = k.getComponentType();

        System.out.println(String.format("Toto pole je typu: %s",aK.getTypeName()));

        //getName vracia divnosti ak to neni jednorozmerne pole
        System.out.println(String.format("Toto pole je typu: %s",aK.getName()));
    }
}
