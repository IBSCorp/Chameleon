package ru.ibsqa.qualit.sap.driver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {


    public static void delayInsec(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

}
