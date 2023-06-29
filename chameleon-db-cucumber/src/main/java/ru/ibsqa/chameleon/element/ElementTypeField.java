package ru.ibsqa.chameleon.element;

@ElementType(name = "Field", parentContexts = {"Connection", "Query", "Params", "Result"}, errorMessage = "Поле c именем [%s] не уникально")
public class ElementTypeField implements IElementType {

}