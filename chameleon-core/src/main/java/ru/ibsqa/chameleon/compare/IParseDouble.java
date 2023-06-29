package ru.ibsqa.chameleon.compare;

import ru.ibsqa.chameleon.i18n.ILocaleManager;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public interface IParseDouble {
    default double parseDouble(String value) {
        try {
            if (Objects.isNull(value) || value.isEmpty()) {
                return 0d;
            }
            if (value.endsWith("L") || value.endsWith("l")) {
                return (double)Long.parseLong(value.substring(0, value.length() - 1));
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            fail(ILocaleManager.message("numberFormatErrorMessage", value));
        }
        return 0d;
    }
}
