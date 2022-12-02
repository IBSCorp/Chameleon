package ru.ibsqa.qualit.utils.waiting;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class WaitingUtils {

    public boolean waiting(long timeout, Supplier<Boolean> supplier) {
        long endtime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endtime) {
            if (supplier.get()) {
                return true;
            }
        }
        return false;
    }

}
