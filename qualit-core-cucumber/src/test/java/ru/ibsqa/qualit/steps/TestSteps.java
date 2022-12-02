package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.data.PriorityTestObject;
import ru.ibsqa.qualit.data.TestObject;
import ru.ibsqa.qualit.data.TestObjectWithSetter;
import ru.ibsqa.qualit.data.plain.*;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.ibsqa.qualit.data.plain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestSteps extends AbstractSteps {
    private TestObject testObject;

    @Тогда("^сбросить сохранённые объекты$")
    public void reset() {
        testObject = null;
    }

    @Тогда("^создан и провалидирован тестовый объект с заголовком \"(.+?)\"$")
    public void createdAndValidatedTestObject(String value) {
        createdTestObject(value);
        validatedTestObject("Тестовый заголовок");
    }

    @Тогда("^создан и провалидирован тестовый объект для проверки приоритезации с заголовком \"(.+?)\"$")
    public void createdAndValidatedPriorityTestObject(PriorityTestObject value) {
        assertNotNull(value);
        assertEquals("Тестовый заголовок", value.title);
    }

    @Тогда("^создан тестовый объект с заголовком \"(.+?)\"$")
    public void createdTestObject(String value) {
        TestObject to = new TestObject(value);
        assertNotNull(to);
        testObject = to;
    }

    @Тогда("^заголовок тестового объекта равен \"(.+?)\"$")
    public void validatedTestObject(String value) {
        assertNotNull(testObject);
        assertEquals(value, testObject.title);
    }

    @Тогда("^конвертация поля с помощью метода-сеттера:$")
    public void stepFillFieldSetter(List<TestObjectWithSetter> fields) {
        assertNotNull(fields);
        assertEquals(1, fields.size());

        fields.forEach(o -> {
            assertNotNull(o.getField1());
        });

        assertEquals("qwertyui_setted", fields.get(0).getField1());
    }

    @Тогда("^конвертация и валидация одного поля в список объектов:$")
    public void stepFillField1(List<PlainObject1> fields) {
        assertNotNull(fields);
        assertEquals(2, fields.size());

        fields.forEach(o -> {
            assertNotNull(o.getField1());
        });

        assertEquals("qwertyui", fields.get(0).getField1());
        assertEquals("111", fields.get(1).getField1());
    }

    @Когда("^конвертация и валидация двух полей в список объектов:$")
    public void stepFillField2(List<PlainObject2> fields) {
        assertNotNull(fields);
        assertEquals(2, fields.size());

        fields.forEach(o -> {
            assertNotNull(o.getField1());
            assertNotNull(o.getField2());
        });

        assertEquals("qwer", fields.get(0).getField1());
        assertEquals("tyui", fields.get(0).getField2());
        assertEquals("111", fields.get(1).getField1());
        assertEquals("222", fields.get(1).getField2());
    }

    @Когда("^конвертация и валидация нескольких полей в список объектов:$")
    public void stepFillField3(List<PlainObject4> fields) {
        assertNotNull(fields);
        assertEquals(2, fields.size());

        fields.forEach(o -> {
            assertNotNull(o.getField1());
            assertNotNull(o.getField2());
            assertNotNull(o.getField3());
            assertNotNull(o.getField4());
        });

        assertEquals("qw", fields.get(0).getField1());
        assertEquals("er", fields.get(0).getField2());
        assertEquals("ty", fields.get(0).getField3());
        assertEquals("ui", fields.get(0).getField4());
        assertEquals("111", fields.get(1).getField1());
        assertEquals("222", fields.get(1).getField2());
        assertEquals("333", fields.get(1).getField3());
        assertEquals("444", fields.get(1).getField4());
    }

    @Тогда("^конвертация и валидация одного поля в список непростых объектов:$")
    public void stepFillField1Complex(List<ComplexObject1> fields) {
        assertNotNull(fields);
        assertEquals(2, fields.size());

        fields.forEach(o -> {
            assertNotNull(o.getField1());
        });

        assertEquals("qwertyui", fields.get(0).getField1().getField1());
        assertEquals("111", fields.get(1).getField1().getField1());
    }

    @Когда("^конвертация и валидация двух полей в список непростых объектов:$")
    public void stepFillField2Complex(List<ComplexObject2> fields) {
        assertNotNull(fields);
        assertEquals(2, fields.size());

        fields.forEach(o -> {
            assertNotNull(o.getField1());
            assertNotNull(o.getField2());
        });

        assertEquals("qwer", fields.get(0).getField1().getField1());
        assertEquals("tyui", fields.get(0).getField2().title);
        assertEquals("111", fields.get(1).getField1().getField1());
        assertEquals("222", fields.get(1).getField2().title);
    }

    @Когда("^конвертация и валидация нескольких полей в список непростых объектов:$")
    public void stepFillField3Complex(List<ComplexObject4> fields) {
        assertNotNull(fields);
        assertEquals(2, fields.size());

        fields.forEach(o -> {
            assertNotNull(o.getField1());
            assertNotNull(o.getField2());
            assertNotNull(o.getField3());
            assertNotNull(o.getField4());
        });

        assertEquals("qw", fields.get(0).getField1());
        assertEquals("er", fields.get(0).getField2().getField1());
        assertEquals("ty", fields.get(0).getField3().title);
        assertEquals("ui", fields.get(0).getField4().title);
        assertEquals("111", fields.get(1).getField1());
        assertEquals("222", fields.get(1).getField2().getField1());
        assertEquals("333", fields.get(1).getField3().title);
        assertEquals("444", fields.get(1).getField4().title);
    }
}
