package ru.ibsqa.qualit.converter;

import ru.ibsqa.qualit.converters.types.ITypeConverter;
import ru.ibsqa.qualit.data.PriorityTestObject;
import org.springframework.stereotype.Component;

@Component
public class PriorityTestObjectOrdinaryConverter implements ITypeConverter<PriorityTestObject> {
    @Override
    public PriorityTestObject convert(String value) {
        throw new RuntimeException("Был вызван некорректный конвертер");
    }
}
