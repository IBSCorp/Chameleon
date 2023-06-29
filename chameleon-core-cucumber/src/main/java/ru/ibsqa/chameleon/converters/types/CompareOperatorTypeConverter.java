package ru.ibsqa.chameleon.converters.types;

import ru.ibsqa.chameleon.steps.CompareOperatorEnum;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class CompareOperatorTypeConverter implements ITypeConverter<CompareOperatorEnum> {

    @Override
    public CompareOperatorEnum convert(String value) {
        return CompareOperatorEnum.fromString(value);
    }
}
