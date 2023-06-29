package ru.ibsqa.chameleon.element;

@ElementType(name = "Request", parentContexts = {"Endpoint"}, findGrandChildren = true, errorMessage = "API запрос c именем [%s] не уникален")
public class ElementTypeRequest implements IElementType {

}