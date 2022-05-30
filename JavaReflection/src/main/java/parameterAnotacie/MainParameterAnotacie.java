package parameterAnotacie;

import parameterAnotacie.anotacie.Anotacie.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class MainParameterAnotacie {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        //vytvorim instanciu
        BestGamesFinder bg = new BestGamesFinder();

        /*
        V klase BestGamesFinder je niekolko metod a nieje na prvy pohlad jasne ako spolu suvisia a na čo si dávať pozor.
        Ak by sme chceli spustit kod tejto klasy musime zapisať všetko čo je nižšie

        //z databazy potiahnem nazvy hier
        Set<String> hry = bg.getAllGames();

        //hra to rating
        //hra to cena
        Map<String, Float> ratingHier = bg.getGameToRating(hry);
        Map<String, Float> cenyHier = bg.getGameToPrice(hry);

        //logika čo vypočíta score, treba si dať pozor na poradie zarania parametrov
        SortedMap<Double, String> skoreHry = bg.scoreGames(cenyHier,ratingHier);

        //podla istej logiky zoradi hry podla top (pomer cena rating)
        List<String> topHry = bg.getTopGames(skoreHry);
        System.out.println(topHry);
         */
        //vypisanie vysledku


        //použitie s anotaciami
        //vdaka komu že execute metody sú T tak tento list nemusim kastovať
        List<String> topHry = execute(bg);

        //System.out.println(topHry);

        //SQL
        SqlQueryBuilder sql = new SqlQueryBuilder(Arrays.asList("1","2","3"),10,"Product",Arrays.asList("cena","vykon"));

        String resutl = execute(sql);
        System.out.println(resutl);
    }

    //metoda ktora vráti nejaky objekt,list... podla toho na akej klase budem metodu volať
    //logiku vytvorena v tejto klase (MainParameterAnotacie) je pouzitelna na hociaku strukturu ktora pouziva anotacie Operation a ZavisiNa
    public static  <T> T execute (Object instancia) throws InvocationTargetException, IllegalAccessException {
        Class<?> klas = instancia.getClass();

        //urobim si mapu metod s hodnotami ich Operation anotacii
        Map<String, Method> operaciaToMetoda = operaciaToMetoda(klas);

        //najdem finalnú metodu (to je tá od ktorej už ziadna ina metoda nezávisí)
        Method finalnaMetoda = najdiFinalResultMetodu(klas);

        //potreben ak metoda potrebuje hodnotu nejakeho fieldu
        Map<String, Field> inputToField = inputToField(klas);

        //najhlavnejšia metoda celej logiky
        return (T) executePodlaZavislosti(instancia,operaciaToMetoda,finalnaMetoda,inputToField);
    }

    //HLAVNA METODA
    private static Object executePodlaZavislosti(Object instancia,
                                                 Map<String, Method> operaciaToMetoda,
                                                 Method aktualnaMetoda,
                                                 Map<String, Field> inputToField) throws InvocationTargetException, IllegalAccessException {
        //parametre metoda ktora sa bude volať, použie sa rekurzia takže ako prva sa nakoniec zavola metoda ktora žiadne dependencie nemá
        //a až ako posledná sa zavolá finalna metoda poslana do tejto metodu ako parameter
        List<Object> parameterValues = new ArrayList<>(aktualnaMetoda.getParameterCount());

        for (Parameter p : aktualnaMetoda.getParameters()) {
            //parameter ktorý vložime do parameterValues
            Object param = null;

            //ak param od niečoho zavisi zavolam znova metodu v ktorej som teraz (executePodlaZavislosti) rekurzivne
            if (p.isAnnotationPresent(ZavisiNa.class)) {
                //hodnota (value) ZavisiNa anotacie
                String hodnotaZavisiNa = p.getAnnotation(ZavisiNa.class).value();

                //Metoda na ktorej závisi parameter z metody ktoru sa teraz snazim spustit
                Method metodaNaKtorejZavisiParameter = operaciaToMetoda.get(hodnotaZavisiNa);

                //takže najskôr idem vykonať metodu na ktorej závisi parameter metody ktoru som sa snazil executnut v pred
                //chadzajucom kroku!
                param = executePodlaZavislosti(instancia, operaciaToMetoda, metodaNaKtorejZavisiParameter, inputToField);
            } else if (p.isAnnotationPresent(Input.class)) {
                //ROZSIRENIE PRE INPUT ANOTACIU PRI PARAMETRI
                //ak má anotaciu vytiahnem si jej hodnotu, s nej potom field a na zaklade instancie jeho hodnotu pošlem ako parameter
                String hodnotaInputAnotacie = p.getAnnotation(Input.class).value();
                Field pole = inputToField.get(hodnotaInputAnotacie);
                pole.setAccessible(true);

                //hodnota sa vytiahne field.get(instancia)
                param = pole.get(instancia);
            }

            //ak param nema anotaciu ZavisiNa tak ho rovno pridam do listu
            parameterValues.add(param);
        }

        //zavolame metodu, realne sa najskor zavolá tá posledna ktora už nemá ziadnu zavislost
        return aktualnaMetoda.invoke(instancia,parameterValues.toArray());
    }

    private static Map<String, Method> operaciaToMetoda (Class<?> klas) {
        Map<String, Method> operaciaToMetoda = new HashMap<>();
        for (Method m : klas.getDeclaredMethods()) {
           //preskočime na dalšie aj aktualna nieje anotovana s operation
            if (!m.isAnnotationPresent(Operation.class)) {
                continue;
            }

            //potiahneme celu anotaciu
            Operation op = m.getAnnotation(Operation.class);

            //vložime do mapy value anotacie ako kluc a metodu ako hodnotu
            operaciaToMetoda.put(op.value(),m);
        }
        return operaciaToMetoda;
    }

    private static Method najdiFinalResultMetodu(Class<?> klas) {
        for (Method m : klas.getDeclaredMethods()) {
            if (m.isAnnotationPresent(FinalResult.class)) {
                return m;
            }
        }

        throw new RuntimeException("Ziadna metoda anotovana s FinalResult");
    }

    //pomocna metoda namapovanie Input anotacie k fieldom
    private static Map<String, Field> inputToField (Class<?> klas) {
        Map<String, Field> inputToField = new HashMap<>();
        for (Field f : klas.getDeclaredFields()) {
            if (f.isAnnotationPresent(Input.class)) {
                String inputValue = f.getAnnotation(Input.class).value();
                inputToField.put(inputValue,f);
            }
        }
        return inputToField;
    }
}
