package ru.ibsqa.chameleon.data.plain;

import ru.ibsqa.chameleon.data.TestObject;
import lombok.Getter;

public class ComplexObject2 {
    @Getter
    private PlainObject1 field1;
    @Getter
    private TestObject field2;
}
