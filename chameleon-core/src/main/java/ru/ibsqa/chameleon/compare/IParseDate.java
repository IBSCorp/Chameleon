package ru.ibsqa.chameleon.compare;

import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public interface IParseDate {
    default Date parseDate(String value) {
        final String varName = ILocaleManager.message("variableDateFormat");
        final Object formatPattern = SpringUtils.getBean(IVariableStorage.class).getVariable(varName);
        if (Objects.isNull(formatPattern)) {
            fail(ILocaleManager.message("wrongDateFormat", varName));
        }
        final DateFormat format = new SimpleDateFormat(formatPattern.toString());
        try {
            return format.parse(value);
        } catch (ParseException e) {
            fail(ILocaleManager.message("errorDateFormat", varName));
        }
        return null;
    }
}
