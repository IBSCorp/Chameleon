package ru.ibsqa.chameleon.converters.types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("rawtypes")
@Component
public class TypeConverterManagerImpl implements ITypeConverterManager {
    private final Map<Class, ITypeConverter> converters = new HashMap<>();

    @Autowired
    private void collectTypeConverters(List<ITypeConverter> allConverters) {
        for (ITypeConverter lstConv : allConverters) {
            Class targetClass = lstConv.getTargetClass();

            if (converters.containsKey(targetClass)) {
                ITypeConverter mapConv = converters.get(targetClass);

                if (lstConv.getClass().isAnnotationPresent(Primary.class)) {
                    if (mapConv.getClass().isAnnotationPresent(Primary.class)) {
                        fail(String.format(
                                "В системе уже зарегистрирован конвертер в класс %s (%s) с аннотацией @Primary. " +
                                        "Невозможно зарегистрировать второй конвертер в этот класс (%s) с такой же аннотацией.",
                                targetClass.getTypeName(), mapConv.getClass().getTypeName(), lstConv.getClass().getTypeName()
                        ));
                    } else {
                        converters.put(targetClass, lstConv);
                    }
                } else {
                    if (!mapConv.getClass().isAnnotationPresent(Primary.class)) {
                        fail(String.format(
                                "В системе уже зарегистрирован конвертер в класс %s (%s) без аннотации @Primary. " +
                                        "Если нужно установить другой конвертер в этот класс (%s) пометьте его аннотацией @Primary.",
                                targetClass.getTypeName(), mapConv.getClass().getTypeName(), lstConv.getClass().getTypeName()
                        ));
                    }
                }
            } else {
                converters.put(targetClass, lstConv);
            }
        }
    }

    @Override
    public Object convert(String value, Class targetClass) {
        if (!converters.containsKey(targetClass)) {
            fail("В системе не зарегистрирован конвертер для класса " + targetClass.getName());
        }

        return converters.get(targetClass).convert(value);
    }
}
