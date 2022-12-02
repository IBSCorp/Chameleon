package ru.ibsqa.qualit.converter;

import ru.ibsqa.qualit.converters.types.ITypeConverter;
import ru.ibsqa.qualit.data.plain.PlainObject1;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class PlainObject1Converter implements ITypeConverter<PlainObject1> {
    @Override
    public PlainObject1 convert(String value) {
        PlainObject1 o = new PlainObject1();
        o.setField1(value);
        return o;
    }
}
