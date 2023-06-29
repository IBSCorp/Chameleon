package ru.ibsqa.chameleon.element;

@ElementType(name = "Response", parentContexts = {"Endpoint"}, findGrandChildren = true, errorMessage = "API ответ c именем [%s] не уникален")
public class ElementTypeResponse implements IElementType {

}