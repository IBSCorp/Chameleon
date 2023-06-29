package ru.ibsqa.chameleon.converter;

import ru.ibsqa.chameleon.converters.types.ITypeConverter;
import ru.ibsqa.chameleon.data.PriorityTestObject;
import org.springframework.stereotype.Component;

@Component
public class PriorityTestObjectOrdinaryConverter implements ITypeConverter<PriorityTestObject> {
    @Override
    public PriorityTestObject convert(String value) {
        throw new RuntimeException("Был вызван некорректный конвертер");
    }
}
