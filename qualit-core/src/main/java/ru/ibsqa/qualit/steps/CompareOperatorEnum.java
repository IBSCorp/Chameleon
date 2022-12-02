package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.storage.IVariableStorage;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public enum CompareOperatorEnum {
    EQUALS("равно"),
    NOT_EQUALS("не равно"),
    CONTAINS("содержит значение"),
    NOT_CONTAINS("не содержит значение"),
    STARTS_WITH("начинается с"),
    NOT_STARTS_WITH("не начинается с"),
    ENDS_WITH("оканчивается на"),
    NOT_ENDS_WITH("не оканчивается на"),
    MATCHS("соответствует"),
    NOT_MATCHS("не соответствует"),
    EQUALS_IGNORE_CASE("равно игнорируя регистр"),
    NOT_EQUALS_IGNORE_CASE("не равно игнорируя регистр"),
    EQUALS_IGNORE_SPACES("равно игнорируя пробелы"),
    NOT_EQUALS_IGNORE_SPACES("не равно игнорируя пробелы"),
    LENGTH_EQUALS("по длине равно"),
    LENGTH_NOT_EQUALS("по длине не равно"),
    LENGTH_GREATER("по длине больше"),
    LENGTH_GREATER_OR_EQUALS("по длине не меньше"),
    LENGTH_LOWER("по длине меньше"),
    LENGTH_LOWER_OR_EQUALS("по длине не больше"),
    GREATER("больше"),
    GREATER_OR_EQUALS("больше или равно"),
    LOWER("меньше"),
    LOWER_OR_EQUALS("меньше или равно"),
    EARLIER_OR_EQUALS("раньше или равно"),
    LATER_OR_EQUALS("позже или равно"),
    LATER("позже"),
    EARLIER("раньше")
    ;
    private String value;

    private static ILocaleManager localeManager;

    private static String message(String messageName, Object... arguments) {
        if (Objects.isNull(localeManager)) {
            localeManager = SpringUtils.getBean(ILocaleManager.class);
        }
        return localeManager.getMessage(messageName, arguments);
    }

    CompareOperatorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static CompareOperatorEnum fromString(String text) {
        for (CompareOperatorEnum b : CompareOperatorEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public String buildErrorMessage(String prefix, String actual, String expected) {
        String message = message("check" + this.name(), actual, expected);
        return (Objects.nonNull(prefix) ? (prefix + ": ") : "") + message;
    }

    public boolean checkValue(String actual, String expected) {
        switch (this) {
            case EQUALS:
                return StringUtils.equals(actual, expected);
            case NOT_EQUALS:
                return !StringUtils.equals(actual, expected);
            case CONTAINS:
                return StringUtils.contains(actual, expected);
            case NOT_CONTAINS:
                return !StringUtils.contains(actual, expected);
            case STARTS_WITH:
                return StringUtils.startsWith(actual, expected);
            case NOT_STARTS_WITH:
                return !StringUtils.startsWith(actual, expected);
            case ENDS_WITH:
                return StringUtils.endsWith(actual, expected);
            case NOT_ENDS_WITH:
                return !StringUtils.endsWith(actual, expected);
            case MATCHS:
                return StringUtils.defaultString(actual).matches(StringUtils.defaultString(expected));
            case NOT_MATCHS:
                return !StringUtils.defaultString(actual).matches(StringUtils.defaultString(expected));
            case EQUALS_IGNORE_CASE:
                return StringUtils.equalsIgnoreCase(actual, expected);
            case NOT_EQUALS_IGNORE_CASE:
                return !StringUtils.equalsIgnoreCase(actual, expected);
            case EQUALS_IGNORE_SPACES:
                return clearSpaces(actual).equals(clearSpaces(expected));
            case NOT_EQUALS_IGNORE_SPACES:
                return !clearSpaces(actual).equals(clearSpaces(expected));
            case LENGTH_EQUALS:
                return StringUtils.defaultString(actual).length() == parseInt(expected);
            case LENGTH_NOT_EQUALS:
                return StringUtils.defaultString(actual).length() != parseInt(expected);
            case LENGTH_GREATER:
                return StringUtils.defaultString(actual).length() > parseInt(expected);
            case LENGTH_GREATER_OR_EQUALS:
                return StringUtils.defaultString(actual).length() >= parseInt(expected);
            case LENGTH_LOWER:
                return StringUtils.defaultString(actual).length() < parseInt(expected);
            case LENGTH_LOWER_OR_EQUALS:
                return StringUtils.defaultString(actual).length() <= parseInt(expected);
            // работа с числами TODO: добавить возможность указывать формат числа
            case GREATER:
                return parseDouble(actual) > parseDouble(expected);
            case GREATER_OR_EQUALS:
                return parseDouble(actual) >= parseDouble(expected);
            case LOWER:
                return parseDouble(actual) < parseDouble(expected);
            case LOWER_OR_EQUALS:
                return parseDouble(actual) <= parseDouble(expected);
            case LATER_OR_EQUALS:
                return actual.equals(expected) || compareDate(actual, expected, CompareOperatorEnum.LATER);
            case EARLIER_OR_EQUALS:
                return actual.equals(expected) || compareDate(actual, expected, CompareOperatorEnum.EARLIER);
            case LATER:
                return compareDate(actual, expected, CompareOperatorEnum.LATER);
            case EARLIER:
                return compareDate(actual, expected, CompareOperatorEnum.EARLIER);
            default:
                return false;
        }
    }

    private static String clearSpaces(String value) {
        return Objects.isNull(value) ? null : value.trim().replaceAll("[\\s\\p{Z}]+", "");
    }

    public static double parseDouble(String value) {
        try {
            return (Objects.isNull(value) || value.isEmpty()) ? 0d : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            fail(message("numberFormatErrorMessage", value));
        }
        return 0d;
    }

    public static int parseInt(String value) {
        try {
            return (Objects.isNull(value) || value.isEmpty()) ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            fail(message("numberFormatErrorMessage", value));
        }
        return 0;
    }

    private boolean compareDate(String actual, String expected, CompareOperatorEnum dateCompareOperatorEnum) {
        if (SpringUtils.getBean(IVariableStorage.class).getVariable("формат_даты") == null) {
            throw new AssertionError("Не определен формат даты. Необходимо задать формат в блоке Тестовые данные, в переменной - формат_даты");
        }
        DateFormat format = new SimpleDateFormat(SpringUtils.getBean(IVariableStorage.class).getVariable("формат_даты").toString());
        try {
            Date dateActual = format.parse(actual);
            Date dateExpected = format.parse(expected);
            if (CompareOperatorEnum.LATER.equals(dateCompareOperatorEnum)){
                return dateActual.after(dateExpected);
            }else if (CompareOperatorEnum.EARLIER.equals(dateCompareOperatorEnum)){
                return dateActual.before(dateExpected);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new AssertionError("Ошибка при обработке даты. Необходимо задать формат в блоке Тестовые данные, в переменной - формат_даты");
    }


}
