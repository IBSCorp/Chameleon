package ru.ibsqa.qualit.data.plain;

import ru.ibsqa.qualit.data.TestObject;
import lombok.Getter;

public class ComplexObject2 {
    @Getter
    private PlainObject1 field1;
    @Getter
    private TestObject field2;
}
