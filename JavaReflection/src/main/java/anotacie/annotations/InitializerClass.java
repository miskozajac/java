package anotacie.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Retention(RetentionPolicy.RUNTIME) aby sme k anotacii mohli pristúpiť v runtime
//@Target(ElementType.TYPE) limituje anotaciu len pre klasy
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InitializerClass {

}
