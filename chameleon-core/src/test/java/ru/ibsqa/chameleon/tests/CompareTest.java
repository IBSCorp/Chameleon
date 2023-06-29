package ru.ibsqa.chameleon.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.chameleon.compare.ICompareManager;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import ru.ibsqa.chameleon.utils.spring.ChameleonSpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(ChameleonSpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class CompareTest {

    @Autowired
    private ICompareManager compareManager;

    @Autowired
    private IVariableStorage variableStorage;

    @Test
    public void compareCheck() {
        assertTrue(compareManager.checkValue("равно", "1", "1"));
        assertFalse(compareManager.checkValue("равно", "1", "2"));

        assertTrue(compareManager.checkValue("не равно", "1", "2"));
        assertFalse(compareManager.checkValue("не равно", "1", "1"));

        assertTrue(compareManager.checkValue("содержит значение", "qwerty", "ert"));
        assertFalse(compareManager.checkValue("содержит значение", "qwerty", "dfg"));

        assertTrue(compareManager.checkValue("не содержит значение", "qwerty", "dfg"));
        assertFalse(compareManager.checkValue("не содержит значение", "qwerty", "ert"));

        assertTrue(compareManager.checkValue("начинается с", "qwerty", "qwe"));
        assertFalse(compareManager.checkValue("начинается с", "qwerty", "dfg"));

        assertTrue(compareManager.checkValue("не начинается с", "qwerty", "dfg"));
        assertFalse(compareManager.checkValue("не начинается с", "qwerty", "qwe"));

        assertTrue(compareManager.checkValue("оканчивается на", "qwerty", "rty"));
        assertFalse(compareManager.checkValue("оканчивается на", "qwerty", "dfg"));

        assertTrue(compareManager.checkValue("не оканчивается на", "qwerty", "dfg"));
        assertFalse(compareManager.checkValue("не оканчивается на", "qwerty", "rty"));

        assertTrue(compareManager.checkValue("соответствует", "qwerty", "\\w+"));
        assertFalse(compareManager.checkValue("соответствует", "qwerty", "\\d+"));

        assertTrue(compareManager.checkValue("не соответствует", "qwerty", "\\d+"));
        assertFalse(compareManager.checkValue("не соответствует", "qwerty", "\\w+"));

        assertTrue(compareManager.checkValue("равно игнорируя регистр", "ЙЦУКЕН", "йцукен"));
        assertFalse(compareManager.checkValue("равно игнорируя регистр", "ЙЦУКЕН", "фыва"));

        assertTrue(compareManager.checkValue("не равно игнорируя регистр", "ЙЦУКЕН", "фыва"));
        assertFalse(compareManager.checkValue("не равно игнорируя регистр", "ЙЦУКЕН", "йцукен"));

        assertTrue(compareManager.checkValue("равно игнорируя пробелы", "ЙЦ УК ЕН", "ЙЦУ КЕН"));
        assertFalse(compareManager.checkValue("равно игнорируя пробелы", "ЙЦ УК ЕН", "ФЫ ВА"));

        assertTrue(compareManager.checkValue("не равно игнорируя пробелы", "ЙЦ УК ЕН", "ФЫ ВА"));
        assertFalse(compareManager.checkValue("не равно игнорируя пробелы", "ЙЦ УК ЕН", "ЙЦУ КЕН"));

        assertTrue(compareManager.checkValue("равно", "1", "1"));
        assertFalse(compareManager.checkValue("равно", "1", "2"));

        assertTrue(compareManager.checkValue("не равно", "1", "2"));
        assertFalse(compareManager.checkValue("не равно", "1", "1"));

        assertTrue(compareManager.checkValue("по длине равно", "qwerty", "6"));
        assertFalse(compareManager.checkValue("по длине равно", "qwerty", "10"));

        assertTrue(compareManager.checkValue("по длине не равно", "qwerty", "10"));
        assertFalse(compareManager.checkValue("по длине не равно", "qwerty", "6"));

        assertTrue(compareManager.checkValue("по длине больше", "qwerty", "1"));
        assertFalse(compareManager.checkValue("по длине больше", "qwerty", "10"));

        assertTrue(compareManager.checkValue("по длине не меньше", "qwerty", "5"));
        assertTrue(compareManager.checkValue("по длине не меньше", "qwerty", "6"));
        assertFalse(compareManager.checkValue("по длине не меньше", "qwerty", "10"));

        assertTrue(compareManager.checkValue("по длине меньше", "qwerty", "7"));
        assertFalse(compareManager.checkValue("по длине меньше", "qwerty", "3"));

        assertTrue(compareManager.checkValue("по длине не больше", "qwerty", "6"));
        assertTrue(compareManager.checkValue("по длине не больше", "qwerty", "7"));
        assertFalse(compareManager.checkValue("по длине не больше", "qwerty", "3"));

        assertTrue(compareManager.checkValue("больше", "10", "5"));
        assertFalse(compareManager.checkValue("больше", "1", "5"));

        assertTrue(compareManager.checkValue("больше или равно", "10", "5"));
        assertTrue(compareManager.checkValue("больше или равно", "5", "5"));
        assertFalse(compareManager.checkValue("больше или равно", "1", "5"));

        assertTrue(compareManager.checkValue("меньше", "1", "5"));
        assertFalse(compareManager.checkValue("меньше", "10", "5"));

        assertTrue(compareManager.checkValue("меньше или равно", "1", "5"));
        assertTrue(compareManager.checkValue("меньше или равно", "5", "5"));
        assertFalse(compareManager.checkValue("меньше или равно", "10", "5"));

        variableStorage.setVariable("формат_даты", "dd.MM.yyyy");

        assertTrue(compareManager.checkValue("позже", "24.02.2022", "23.02.2022"));
        assertFalse(compareManager.checkValue("позже", "24.02.2022", "24.02.2022"));
        assertFalse(compareManager.checkValue("позже", "24.02.2022", "25.02.2022"));

        assertTrue(compareManager.checkValue("раньше", "24.02.2022", "25.02.2022"));
        assertFalse(compareManager.checkValue("раньше", "24.02.2022", "24.02.2022"));
        assertFalse(compareManager.checkValue("раньше", "24.02.2022", "23.02.2022"));

        assertTrue(compareManager.checkValue("позже или равно", "24.02.2022", "23.02.2022"));
        assertTrue(compareManager.checkValue("позже или равно", "24.02.2022", "24.02.2022"));
        assertFalse(compareManager.checkValue("позже или равно", "24.02.2022", "25.02.2022"));

        assertTrue(compareManager.checkValue("раньше или равно", "24.02.2022", "25.02.2022"));
        assertTrue(compareManager.checkValue("раньше или равно", "24.02.2022", "24.02.2022"));
        assertFalse(compareManager.checkValue("раньше или равно", "24.02.2022", "23.02.2022"));
    }

    @Test
    public void compareUnknown() {
        assertEquals(
                "не определен оператор сравнения [неизвестная операция]",
                assertThrows(AssertionError.class, () ->
                        compareManager.checkValue("неизвестная операция", "1", "1")
                ).getMessage()
        );
    }

    @Test
    public void compareDataFormatEmpty() {
        variableStorage.clearVariables();
        assertEquals(
                "Не определен формат даты. Необходимо задать формат в блоке Тестовые данные, в переменной - [формат_даты]",
                assertThrows(AssertionError.class, () ->
                        compareManager.checkValue("позже", "11111", "22222")
                ).getMessage()
        );
    }

    @Test
    public void compareWrongData() {
        variableStorage.setVariable("формат_даты", "dd.MM.yyyy");
        assertEquals(
                "Ошибка при обработке даты. Необходимо задать формат в блоке Тестовые данные, в переменной - [формат_даты]",
                assertThrows(AssertionError.class, () ->
                        compareManager.checkValue("позже", "11111", "22222")
                ).getMessage()
        );
    }

    @Test
    public void buildErrorMessage() {
        assertEquals("текущее значение [2] не равно ожидаемому значению [1]",
                compareManager.buildErrorMessage("равно", null, "2", "1"));
        assertEquals("Проверка размера коллекции: текущее значение [2] не равно ожидаемому значению [1]",
                compareManager.buildErrorMessage("равно", "Проверка размера коллекции", "2", "1"));

        assertEquals("для значения [2] не выполняется условие [тестовая операция] [1]",
                compareManager.buildErrorMessage("тестовая операция", null, "2", "1"));
    }
}
