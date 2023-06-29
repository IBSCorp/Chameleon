package ru.ibsqa.chameleon.element;

@ElementType(name = "Body", parentContexts = {"Request", "Response"}, errorMessage = "API Body c именем [%s] не уникально")
public class ElementTypeBody implements IElementType {

}