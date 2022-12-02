package ru.ibsqa.qualit.converters;

import ru.ibsqa.qualit.converters.types.ITypeConverterManager;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TypeRegistryConfiguration {

    private static IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);
    private static ITypeConverterManager typeConverterManager = SpringUtils.getBean(ITypeConverterManager.class);

    /**
     * Default data transformer for the parameter from the cucumber step.
     *
     * @param fromValue   - parameter from the cucumber step
     * @param toValueType - type of the class to convert
     *
     * @return transformed object to the <tt>toValueType</tt> class type.
     */
    @DefaultParameterTransformer
    public Object defaultTransformer(String fromValue, Type toValueType) throws ClassNotFoundException {
        return typeConverterManager.convert(evaluateManager.evalVariable(fromValue), Class.forName(toValueType.getTypeName()));
    }

    @DefaultDataTableEntryTransformer
    // Аннотация ниже, чтобы избежать в логе сообщений вида:
    // Some input files use unchecked or unsafe operations.
    // Recompile with -Xlint:unchecked for details.
    @SuppressWarnings("unchecked")
    public <T> T defaultMapTransformer(Map<String, String> fromValue, Type toValueType) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class<?> type = Class.forName(toValueType.getTypeName());
        T object = (T) type.getConstructor().newInstance();
        for (Map.Entry<String, String> e : fromValue.entrySet()) {
            Object value = typeConverterManager.convert(evaluateManager.evalVariable(e.getValue()),
                    Class.forName(type.getDeclaredField(e.getKey()).getType().getTypeName())
            );

            try {
                // Первым делом пробуем вызвать сеттер для поля
                String methodName = "set" + StringUtils.capitalize(e.getKey());
                Method setter = type.getDeclaredMethod(methodName, type.getDeclaredField(e.getKey()).getType());
                setter.invoke(object, value);
            } catch (NoSuchMethodException ex) {
                // Если сеттера нет - пишем в поле напрямую
                Field field = type.getDeclaredField(e.getKey());
                field.setAccessible(true);
                field.set(object, value);
            }
        }
        return object;
    }

    private Map<String, String> replaceInMap(Map<String, String> values) {
        Map<String, String> map = new HashMap<>(values);
        map.entrySet().forEach(entry -> entry.setValue(evaluateManager.evalVariable(entry.getValue())));
        return map;
    }
}
