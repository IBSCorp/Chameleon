package ru.ibsqa.chameleon.context;

public interface IFieldNameResolver {
    String resolveParams(String name) throws SearchElementException;
}
