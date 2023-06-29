package ru.ibsqa.chameleon.utils.delay;

public class DelayUtils {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {

        }
    }

    public static void sleepSec(int seconds) {
        sleep(seconds * 1000);
    }
}
