package ru.ibsqa.chameleon.element;

@ElementType(name = "Field", parentContexts = {"Request", "Response", "Endpoint"}, errorMessage = "Поле c именем [%s] не уникально")
public class ElementTypeField implements IElementType {

}
