package ru.ibsqa.chameleon.definitions.repository;

import java.util.List;
import java.util.NoSuchElementException;

public interface IRepositoryData {
    <ELEMENT extends IRepositoryElement> List<ELEMENT> pickAllElements();
    <ELEMENT extends IRepositoryElement> ELEMENT pickElement(String name, Class<ELEMENT> elementClass);
    default <ELEMENT extends IRepositoryElement> ELEMENT pickElement(String name, Class<ELEMENT> elementType, List<? extends INamedRepositoryElement> list) {
        if (null == list) {
            return null;
        }
        try {
            return (ELEMENT) list
                    .stream()
                    .filter(item -> (null == name && null == item.getName()) || (null != item.getName() && item.getName().equals(name)))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
