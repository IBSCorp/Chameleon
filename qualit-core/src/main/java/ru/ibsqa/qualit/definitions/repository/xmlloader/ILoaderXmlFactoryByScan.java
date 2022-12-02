package ru.ibsqa.qualit.definitions.repository.xmlloader;

import ru.ibsqa.qualit.definitions.repository.IXmlRepositoryType;

import javax.xml.bind.Unmarshaller;

public interface ILoaderXmlFactoryByScan {
    Unmarshaller getUnmarshaller(IXmlRepositoryType repositoryType);
}
