package anotacie.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Repeatable(ExecutionSchedule.class)
@Target(ElementType.METHOD)
public @interface ExecuteOnSchedule {
    int delaySeconds() default 0;
    int periodSeconds();
}
