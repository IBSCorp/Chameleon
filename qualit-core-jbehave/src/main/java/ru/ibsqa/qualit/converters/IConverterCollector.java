package ru.ibsqa.qualit.converters;

import org.jbehave.core.steps.ParameterConverters;

import java.util.List;
import java.util.stream.Collectors;

public interface IConverterCollector {

    List<IConverter> getConverters();

    default List<ParameterConverters.ParameterConverter> getParameterConverters(){
        return getConverters().stream().map(item -> (ParameterConverters.ParameterConverter) item).collect(Collectors.toList());
    }
}
