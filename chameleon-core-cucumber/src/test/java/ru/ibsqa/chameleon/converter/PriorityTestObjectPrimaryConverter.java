package ru.ibsqa.chameleon.converter;

import ru.ibsqa.chameleon.converters.types.ITypeConverter;
import ru.ibsqa.chameleon.data.PriorityTestObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class PriorityTestObjectPrimaryConverter implements ITypeConverter<PriorityTestObject> {
    @Override
    public PriorityTestObject convert(String value) {
        return new PriorityTestObject(value);
    }
}
