package ru.ibsqa.chameleon.element;

@ElementType(name = "Params", parentContexts = {"Request", "Response"}, errorMessage = "API Params c именем [%s] не уникальны")
public class ElementTypeParams implements IElementType {

}