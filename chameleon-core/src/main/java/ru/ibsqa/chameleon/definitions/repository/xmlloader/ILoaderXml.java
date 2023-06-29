package ru.ibsqa.chameleon.definitions.repository.xmlloader;

import lombok.NonNull;
import org.springframework.core.io.Resource;
import ru.ibsqa.chameleon.definitions.repository.ILoader;
import ru.ibsqa.chameleon.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryWrapper;

public interface ILoaderXml extends ILoader {
    IRepositoryWrapper load(@NonNull Resource resource, @NonNull IXmlRepositoryType repositoryType);
}
