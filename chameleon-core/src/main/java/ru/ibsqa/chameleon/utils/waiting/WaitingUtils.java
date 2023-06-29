package ru.ibsqa.chameleon.utils.waiting;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Component
public class WaitingUtils {

    public boolean waiting(long timeout, Supplier<Boolean> supplier) {
        long endtime = System.currentTimeMillis() + timeout;
        do {
            if (supplier.get()) {
                return true;
            }
        } while (System.currentTimeMillis() < endtime);
        return false;
    }

    public boolean waiting(Duration timeout, Supplier<Boolean> supplier) {
        return waiting(timeout.toMillis(), supplier);
    }

}
