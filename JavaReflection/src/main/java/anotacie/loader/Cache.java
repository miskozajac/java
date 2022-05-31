package anotacie.loader;

import anotacie.annotations.ExecuteOnSchedule;
import anotacie.annotations.ScheduledExecutorClass;

@ScheduledExecutorClass
public class Cache {

    @ExecuteOnSchedule(periodSeconds = 5)
    @ExecuteOnSchedule(delaySeconds = 10, periodSeconds = 1)
    public static void reloadCache() {
        System.out.println("Reloading cache");
    }
}
