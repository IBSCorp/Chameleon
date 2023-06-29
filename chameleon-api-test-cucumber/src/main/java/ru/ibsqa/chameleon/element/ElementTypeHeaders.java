package ru.ibsqa.chameleon.element;

@ElementType(name = "Headers", parentContexts = {"Request", "Response"}, errorMessage = "API Headers c именем [%s] не уникальны")
public class ElementTypeHeaders implements IElementType {

}