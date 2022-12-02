package ru.ibsqa.qualit.converters.types;

public interface ITypeConverterManager {
    <T> T convert(String value, Class<T> targetClass);
}
