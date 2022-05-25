package cvicenia;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Constractors {

    public static void main (String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //printConstractorData(Address.class);

        Address adresa = vytvorInstanciu(Address.class,"dolna ulica",10);
        Person osoba = vytvorInstanciu(Person.class,adresa,"Janko",25);

        System.out.println(osoba);
        System.out.println(adresa);
    }

    public static <T> T vytvorInstanciu(Class<T> klas, Object ... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Constructor<?> kon : klas.getDeclaredConstructors()) {
            if (kon.getParameterTypes().length == args.length) {
                return (T)kon.newInstance(args);
            }
        }
        System.out.println("Nenasiel sa ziaden konstruktor");
        return null;
    }

    /*
    ALEBO TROCHU HORSI SPOSOB KDE MUSIM CASTOVAT PRI VOLANI
    public static Object vytvorInstanciu(Class<?> klas, Object ... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Constructor<?> kon : klas.getDeclaredConstructors()) {
            if (kon.getParameterTypes().length == args.length) {
                return kon.newInstance(args);
            }
        }
        System.out.println("Nenasiel sa ziaden konstruktor");
        return null;
    }
     */

    public static void printConstractorData(Class<?> klas) {
        Constructor<?>[] konstruktory = klas.getDeclaredConstructors();
        System.out.println(String.format("Klasa %s má %d deklarované konstruktory",klas.getSimpleName(),konstruktory.length));

        for (int i = 0; i < konstruktory.length; i++) {
            Class<?>[] parametre = konstruktory[i].getParameterTypes();

            /*
            //Tento blok kodu sa dá zapísať do jedneho riadku (vid pod)
            List<String> menaParametrov = new ArrayList<>();
            for (Class<?> k : parametre) {
                menaParametrov.add(k.getSimpleName());
            }
            */
            List<String> menaParametrov = Arrays.stream(parametre).map(typ -> typ.getSimpleName()).collect(Collectors.toList());

            System.out.println(menaParametrov);
        }
    }

    public static class Person {
        private final Address address;
        private final String name;
        private final int age;

        public Person() {
            this.name = "anonymous";
            this.age = 0;
            this.address = null;
        }

        public Person(String name) {
            this.name = name;
            this.age = 0;
            this.address = null;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
            this.address = null;
        }

        public Person(Address address, String name, int age) {
            this.address = address;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "address=" + address +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static class Address {
        private String street;
        private int number;

        public Address(String street, int number) {
            this.street = street;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", number=" + number +
                    '}';
        }
    }
}
