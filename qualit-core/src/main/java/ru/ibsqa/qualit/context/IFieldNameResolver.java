package ru.ibsqa.qualit.context;

public interface IFieldNameResolver {
    String resolveParams(String name) throws SearchElementException;
}
