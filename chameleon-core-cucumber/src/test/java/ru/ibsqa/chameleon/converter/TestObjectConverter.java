package ru.ibsqa.chameleon.converter;

import ru.ibsqa.chameleon.converters.types.ITypeConverter;
import ru.ibsqa.chameleon.data.TestObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class TestObjectConverter implements ITypeConverter<TestObject> {
    @Override
    public TestObject convert(String value) {
        return new TestObject(value);
    }
}
