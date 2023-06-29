package ru.ibsqa.chameleon.definitions.repository.xmlloader;

import ru.ibsqa.chameleon.definitions.repository.IXmlRepositoryType;

import javax.xml.bind.Unmarshaller;

public interface ILoaderXmlFactoryByScan {
    Unmarshaller getUnmarshaller(IXmlRepositoryType repositoryType);
}
