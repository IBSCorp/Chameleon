package ru.ibsqa.qualit.compare;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.storage.IVariableStorage;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

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
