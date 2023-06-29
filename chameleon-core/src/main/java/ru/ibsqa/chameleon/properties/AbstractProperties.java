package ru.ibsqa.chameleon.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;
@Slf4j
public abstract class AbstractProperties {

    // Загруженные ранее свойства
    private Properties properties;

    // Метод возвращает стандартное расположение свойств
    protected abstract String getResourceLocation();

    /**
     * Получить свойства, загруженные из заданной локации.
     * При первом обращении, происходит обращение к ресурсам, в дальнейшем к кешу.
     *
     * @return
     */
    public Properties getProperties(boolean required) {
        if (Objects.isNull(properties)) {
            if (Objects.nonNull(getResourceLocation())) {
                Resource[] resources = SpringUtils.getResources(getResourceLocation());
                if (resources.length > 0) {
                    properties = new Properties();
                    for (Resource resource : resources) {
                        log.info(String.format("Считывание свойств из %s", resource.getFilename()));
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                            Properties currentProperties = new Properties();
                            currentProperties.load(reader);
                            currentProperties.forEach((key, value) -> {
                                if (properties.containsKey(key)) {
                                    log.warn(String.format("Параметр %s со значением %s будет переопределен " +
                                            "значением %s из файла %s. Постарайтесь устранить дублирование параметров.",
                                            key,
                                            properties.get(key),
                                            value,
                                            resource.getFilename()
                                    ));
                                } else {
                                    log.debug(String.format("Параметр %s равен: %s", key, value));
                                }
                            });
                            properties.putAll(currentProperties);
                        } catch (IOException ex) {
                            String message = String.format("Не удалось выполнить загрузку свойств из %s. Ошибка: %s", resource.getFilename(), ex.getMessage());
                            if (required) {
                                fail(message);
                            } else {
                                log.warn(message);
                            }
                        }
                    }
                }
            }
        }
        if (Objects.isNull(properties)) {
            String message = String.format("Не удалось выполнить загрузку свойств из %s", getResourceLocation());
            if (required) {
                fail(message);
            } else {
                log.warn(message);
            }
            return new Properties();
        }

        return properties;
    }

    /**
     * Получить значение свойства
     * @param key
     * @param defaultValue
     * @param required
     * @return
     */
    public String getProperty(String key, String defaultValue, boolean required) {
        String result = Optional.ofNullable(getProperties(required)).map(ps -> ps.getProperty(key, defaultValue)).orElse(null);

        if (Objects.isNull(result)) {
            String message = String.format("Не удалось найти параметр %s в %s", key, getResourceLocation());
            if (required) {
                fail(message);
            } else {
                log.warn(message);
            }
        }

        return result;
    }
}
