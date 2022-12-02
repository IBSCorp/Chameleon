package ru.ibsqa.qualit.converter;

import ru.ibsqa.qualit.converters.types.ITypeConverter;
import ru.ibsqa.qualit.data.PriorityTestObject;
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
