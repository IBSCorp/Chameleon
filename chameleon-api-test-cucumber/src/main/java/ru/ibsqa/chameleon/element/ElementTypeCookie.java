package ru.ibsqa.chameleon.element;

@ElementType(name = "Cookie", parentContexts = {"Request", "Response"}, errorMessage = "API Cookie c именем [%s] не уникальны")
public class ElementTypeCookie implements IElementType {

}