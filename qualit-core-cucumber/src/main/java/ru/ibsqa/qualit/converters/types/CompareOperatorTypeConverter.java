package ru.ibsqa.qualit.converters.types;

import ru.ibsqa.qualit.steps.CompareOperatorEnum;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class CompareOperatorTypeConverter implements ITypeConverter<CompareOperatorEnum> {

    @Override
    public CompareOperatorEnum convert(String value) {
        return CompareOperatorEnum.fromString(value);
    }
}
