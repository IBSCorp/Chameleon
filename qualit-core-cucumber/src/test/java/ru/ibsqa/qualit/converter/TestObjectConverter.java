package ru.ibsqa.qualit.converter;

import ru.ibsqa.qualit.converters.types.ITypeConverter;
import ru.ibsqa.qualit.data.TestObject;
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
