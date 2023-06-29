package ru.ibsqa.chameleon.json.utils;

import ru.ibsqa.chameleon.evaluate.IEvaluateManager;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class DataUtils {

    private static IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

    /**
     * Проверка, что строка является целым числом
     * @param value
     * @return
     */
    public static boolean isInteger(String value) {
        return value.matches("-?\\d+");
    }

    /**
     * Проверка, что строка является JSON
     * @param value
     * @return
     */
    public static boolean isJSON(String value) {
        return null != value && (value.trim().matches("(?s)(?<object>\\{.*\\})") || value.trim().matches("(?s)(?<array>\\[.*\\])"));
    }

    /**
     * Получить данные из файла ресурсов
     * @param value
     * @return
     */
    public static String getDataAsString(String value) {
        if (null == value) {
            return null;
        }

        String text = evaluateManager.evalVariable(value);
        if (!isJSON(text)) {
            InputStream inputStream = null;
            if (!SpringUtils.getResource(text).exists()){
                fail("Не найден файл по пути: " + text);
            }
            try {
                inputStream = SpringUtils.getResource(text).getInputStream();
                Scanner s = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
                text = s.hasNext() ? s.next() : "";
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                fail();
            } finally {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            text = evaluateManager.evalVariable(text);
        }
        return text;

    }
}
