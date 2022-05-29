package anotacie;

import anotacie.annotations.InitializerClass;
import anotacie.annotations.InitializerMethod;
import anotacie.annotations.RetryAnotation;
import anotacie.annotations.ScanPackages;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ScanPackages({"app","app.configs","app.databases"})
public class MainAnotacie {

    public static void main(String[] args) throws Throwable {
        inicializuj();
    }

    public static List<Class<?>> getVsetkyKlasy(String... packages) throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> klasy = new ArrayList<>();

        //zo string nazvu packagov sa potrebujem dostať k Path aby som potom vedel vytiahnut files
        for (String pName : packages) {
            //najdenie relativnej cesty
            String relativePName = pName.replace(".","/");

            //Uniform Resource Identifier
            //na zaklade tohto zistime či je subor ktory čitame tu v projekte alebo v jarku
            URI uniPath = MainAnotacie.class.getResource(relativePName).toURI();

            //package je tu
            if (uniPath.getScheme().equals("file")) {
                //Path ziskam z URI a to ziskam zo string relativneho nazvu packagu teda nazvu so lomitkami
                Path pFullPath = Paths.get(uniPath);
                klasy.addAll(getKlasyPackagu(pFullPath, pName));
            } else if (uniPath.getScheme().equals("jar")) {
                //fileSystem sa vie pozerať do jar filu
                //prázdna mapa je mapa prostreďových premenných
                FileSystem fs = FileSystems.newFileSystem(uniPath, Collections.emptyMap());
                //fs ziska path zo string relativneho package namu
                Path pFullPath = fs.getPath(relativePName);
                klasy.addAll(getKlasyPackagu(pFullPath, pName));

                //fs sa musí zavrieť aby sa potom mohol pouzit znova
                fs.close();
            }
        }
        return klasy;
    }

    private static List<Class<?>> getKlasyPackagu(Path pPath, String pName) throws IOException, ClassNotFoundException {
        List<Class<?>> klasy = new ArrayList<>();

        //ak daný package neexistuje vravim prazdny list
        if (!Files.exists(pPath)) {
            return Collections.emptyList();
        }

        //takto vytiahnem files v pathu, a len RegularFile to znamena vsetko okrem ostatnych packagov, proste normalne (regulerne subory)
        List<Path> files = Files.list(pPath).filter(Files::isRegularFile).collect(Collectors.toList());

        for (Path p : files) {
            String menoFilu = p.getFileName().toString();

            //ak je file klasa tak ju nájdem a ulozim do listu ktorý v tejto metode vraciam
            if (menoFilu.endsWith(".class")) {
                //mám ine usporiadanie filov ako v kurze, musim sem ešte pridať "anotacie." ale nemôžem stým volať inicializuj
                String klasFullName = "anotacie." + pName + "." + menoFilu.replaceFirst(".class","");
                klasy.add(Class.forName(klasFullName));
            }
        }

        return klasy;
    }
    public static void inicializuj() throws Throwable {
        ScanPackages sp = MainAnotacie.class.getAnnotation(ScanPackages.class);

        if (sp == null || sp.value().length == 0) {
            return;
        }

        List<Class<?>> klasy = getVsetkyKlasy(sp.value());

        for (Class<?> k : klasy) {
            if (!k.isAnnotationPresent(InitializerClass.class)) {
                continue;
            }

            //treba si dať pozor či mám správne definovane konstruktory
            Object instancia = k.getDeclaredConstructor().newInstance();

            for (Method m : getAllInitializingMethods(k)) {
                //po pridani anotacie RetryAnotacion už nestaci len zavolať metodu prikazom m.invoke(instancia), treba dopracovať logiku anotacie
                zavolajMetodu(instancia,m);
            }
        }
    }

    private static void zavolajMetodu(Object instancia, Method m) throws Throwable {
        RetryAnotation ra = m.getAnnotation(RetryAnotation.class);

        //pocet pokusov zavolat metodu (okrem toho prvého, teda je to počet repokusov)
        int pocetZavolaniMetody = ra == null ? 0 : ra.pocetOpakovani();

        //
        while (true) {
            try {
                //najskôr skuim normalne zavolať ako doteraz, ak nepôjde tak catch block
                m.invoke(instancia);
                break;
            } catch (InvocationTargetException e) {
                Throwable chybaNaNestovanie = e.getTargetException();

                //funguje to už aj len s if ale pridame aj else časti
                if (pocetZavolaniMetody > 0 && Set.of(ra.testExceptions()).contains(chybaNaNestovanie.getClass())) {
                    pocetZavolaniMetody--;
                    System.out.println("Skusam znova ...");
                    Thread.sleep(ra.dlzkaCakania());
                } else if (ra != null) {
                    //to znamena že s počtom opakovani sme už na nule teda nepodarilo sa
                    throw new Exception(ra.sprava(),chybaNaNestovanie);
                } else {
                    //sitacia kde napr metoda nemá RetryAnotation anotaciu
                    throw chybaNaNestovanie;
                }
            }
        }
    }

    private static List<Method> getAllInitializingMethods(Class<?> klas) {
        List<Method> metodyNaSpustenie = new ArrayList<>();
        for (Method m : klas.getDeclaredMethods()) {
            if (m.isAnnotationPresent(InitializerMethod.class)) {
                metodyNaSpustenie.add(m);
            }
        }
        return metodyNaSpustenie;
    }
}
