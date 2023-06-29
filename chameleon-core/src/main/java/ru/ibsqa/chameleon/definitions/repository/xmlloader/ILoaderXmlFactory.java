package ru.ibsqa.chameleon.definitions.repository.xmlloader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Deprecated
public interface ILoaderXmlFactory {
    JAXBContext getContext() throws JAXBException;
}
