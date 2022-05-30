package parameterAnotacie.anotacie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//anotacie vytvorim v normalnej klase
public class Anotacie {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD,ElementType.PARAMETER})
    public @interface Input {
        String value();
    }

    //táto anotacia prepaja metodu s parametrami inej metody
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Operation {
        String value();
    }

    //pri pouziti tejto anotacie je value rovná tej hodnote ktorú má ina metoda v anotacii Operation
    //použiva sa ak param metody závisi na INEJ METODE!!!
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface ZavisiNa {
        String value();
    }

    //
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface FinalResult {}
}
