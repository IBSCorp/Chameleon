package ru.ibsqa.qualit.definitions.repository.xmlloader;

import lombok.NonNull;
import org.springframework.core.io.Resource;
import ru.ibsqa.qualit.definitions.repository.ILoader;
import ru.ibsqa.qualit.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.qualit.definitions.repository.IRepositoryWrapper;

public interface ILoaderXml extends ILoader {
    IRepositoryWrapper load(@NonNull Resource resource, @NonNull IXmlRepositoryType repositoryType);
}
