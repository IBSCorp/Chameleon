package ru.ibsqa.chameleon.converters.types;

import java.lang.reflect.ParameterizedType;

/**
 * Интерфейс для конвертера "строка => объект"<br/>
 * <br/>
 * Для подключения конвертера из своего проекта нужно создать класс, который будет имплементировать
 * этот интерфейс, и поставить над ним аннотацию <b>@Component</b>:
 *
 * <pre>
 *    {@literal @}Component
 *     public class ExampleTypeConverter implements ITypeConverter {
 *        {@literal @}Override
 *         public Class&lt;?&gt; getTargetClass() {
 *             return Example.class;
 *         }
 *
 *        {@literal @}Override
 *         public Object convert(String value) {
 *             return new Example(value);
 *         }
 *     }
 * </pre>
 */
public interface ITypeConverter<T> {
    /**
     * Метод, возвращающий класс, в который данный конвертер переводит входную строку.
     * <p/>
     * <b>Внимание!</b> По умолчанию, целевой класс достаётся из типа дженерика. <b>Настоятельно не рекомендуется</b> переопределять данный метод.
     */
    @SuppressWarnings("unchecked")
    default Class<T> getTargetClass() {
        try {
            return (Class<T>) Class.forName(
                    ((ParameterizedType) getClass().getGenericInterfaces()[0]).
                            getActualTypeArguments()[0].getTypeName()
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удалось получить целевой класс из дженерика", e);
        }
    }

    /**
     * Метод конвертации входной строки <code>value</code> в объект нужного класса
     */
    T convert(String value);
}
