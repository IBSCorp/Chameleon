package ru.ibsqa.qualit.converters.types;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StringTypeConverter implements ITypeConverter<String> {
    @Autowired
    private IEvaluateManager evaluateManager;

    private final ObjectMapper objectMapper;

    public StringTypeConverter() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public String convert(String value) {
        return objectMapper.convertValue(
                evaluateManager.evalVariable(value),
                objectMapper.constructType(String.class)
        );
    }
}
