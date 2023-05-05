package ru.ibsqa.qualit.compare;

import ru.ibsqa.qualit.i18n.ILocaleManager;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public interface IParseInt {
    default int parseInt(String value) {
        try {
            return (Objects.isNull(value) || value.isEmpty()) ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            fail(ILocaleManager.message("numberFormatErrorMessage", value));
        }
        return 0;
    }
}
