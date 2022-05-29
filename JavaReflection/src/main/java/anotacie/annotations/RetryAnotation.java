package anotacie.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryAnotation {
    //testExceptions obsahuje vsetky exc ktore budeme chcieť opakovať
    Class<? extends Throwable>[] testExceptions() default {Exception.class};
    long dlzkaCakania() default 0;
    String sprava() default "Nevydalo";
    int pocetOpakovani();
}
