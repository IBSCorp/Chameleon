package ru.ibsqa.qualit.converters;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConverterCollectorImpl implements IConverterCollector{

    @Getter
    private List<IConverter> converters = new ArrayList<>();

    @Autowired
    private void collectConverters(List<IConverter> converters){
        this.converters.addAll(converters);
    }
}
