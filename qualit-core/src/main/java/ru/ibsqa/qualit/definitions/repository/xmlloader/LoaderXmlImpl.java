package ru.ibsqa.qualit.definitions.repository.xmlloader;

import lombok.NonNull;
import lombok.val;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import ru.ibsqa.qualit.definitions.repository.IRepositoryData;
import ru.ibsqa.qualit.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.qualit.definitions.repository.IRepositoryWrapper;
import ru.ibsqa.qualit.definitions.repository.RepositoryWrapperImpl;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Component("DefaultLoaderXml")
@Slf4j
public class LoaderXmlImpl implements ILoaderXml {

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IXmlNamespaceFilter filterXml;

    @Autowired
    private ILoaderXmlFactoryByScan loaderXmlFactoryByScan;

    @Getter @Setter
    @Deprecated
    private ILoaderXmlFactory factory;

    @Override
    @Deprecated
    public boolean load(@NonNull IRepositoryWrapper repositoryWrapper, @NonNull String fileName) {
        assertNotNull(factory);
        Unmarshaller unmarshaller;
        try {
            unmarshaller = factory.getContext().createUnmarshaller();
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
            fail(localeManager.getMessage("xmlLoadErrorMessage", fileName));
            return false;
        }
        return unmarshalAndSetData(repositoryWrapper, SpringUtils.getResource(fileName),  unmarshaller);
    }

    @Override
    public IRepositoryWrapper load(@NonNull Resource resource, @NonNull IXmlRepositoryType repositoryType) {
        val repositoryWrapper = new RepositoryWrapperImpl();
        if (!unmarshalAndSetData(repositoryWrapper, resource, repositoryType)) {
            return null;
        }
        return repositoryWrapper;
    }

    /**
     * Загрузка элементов в репозиторий из XML
     * @param repositoryWrapper пустой репозиторий, созданный ранее
     * @param resource ресурс, откуда производится чтение
     * @param unmarshaller JAXB unmarshaller
     * @return
     */
    @Deprecated
    private boolean unmarshalAndSetData(@NonNull IRepositoryWrapper repositoryWrapper, @NonNull Resource resource, @NonNull Unmarshaller unmarshaller) {

        try {
            IRepositoryData data = (IRepositoryData) unmarshaller.unmarshal(resource.getInputStream());
            repositoryWrapper.setData(data);
            log.info(localeManager.getMessage("xmlLoadSuccessMessage", resource.getDescription()));
        } catch (JAXBException | IOException e) {
            log.error(e.getMessage(), e);
            fail(localeManager.getMessage("xmlLoadErrorMessage", "?", resource.getDescription()));
            return false;
        }

        return true;
    }

    /**
     * Загрузка элементов в репозиторий из XML
     * @param repositoryWrapper пустой репозиторий, созданный ранее
     * @param resource ресурс, откуда производится чтение
     * @param repositoryType тип репозитория
     * @return
     */
    private boolean unmarshalAndSetData(@NonNull IRepositoryWrapper repositoryWrapper, @NonNull Resource resource, @NonNull IXmlRepositoryType repositoryType) {

        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            XMLFilter filter = filterXml.getFilter(repositoryType.getNamespace());
            filter.setParent(reader);
            InputSource is = new InputSource(resource.getInputStream());
            SAXSource source = new SAXSource(filter, is);
            Unmarshaller unmarshaller = loaderXmlFactoryByScan.getUnmarshaller(repositoryType);
            IRepositoryData data = (IRepositoryData) unmarshaller.unmarshal(source);
            repositoryWrapper.setData(data);
            log.info(localeManager.getMessage("xmlLoadSuccessMessage", repositoryType.getTypeName(), resource.getDescription()));
        } catch (JAXBException | SAXException | IOException e) {
            log.error(e.getMessage(), e);
            fail(localeManager.getMessage("xmlLoadErrorMessage", resource.getDescription()));
            return false;
        }

        return true;
    }

}
