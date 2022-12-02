package ru.ibsqa.qualit.converters;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class VariableConverter implements ParameterConverter, IConverter {

    @Autowired
    private IEvaluateManager evaluateManager;

    @Override
    public boolean accept(Type type) {
        if (type instanceof Class<?>) {
            return Variable.class.isAssignableFrom((Class<?>) type);
        }
        return false;
    }

    @Override
    public Object convertValue(Object o, Type type) {
        return new Variable(o.toString(), evaluateManager);
    }
}
