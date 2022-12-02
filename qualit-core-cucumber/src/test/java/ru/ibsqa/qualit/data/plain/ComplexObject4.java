package ru.ibsqa.qualit.data.plain;

import ru.ibsqa.qualit.data.PriorityTestObject;
import ru.ibsqa.qualit.data.TestObject;
import lombok.Getter;

public class ComplexObject4 {
    @Getter
    private String field1;
    @Getter
    private PlainObject1 field2;
    @Getter
    private TestObject field3;
    @Getter
    private PriorityTestObject field4;
}
