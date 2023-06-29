package ru.ibsqa.chameleon.definitions.repository;

import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.xmlloader.ILoaderXml;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.utils.xml.IXmlConfigReader;
import ru.ibsqa.chameleon.utils.xml.XmlConfigScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Менеджер репозиториев
 * До версии 3.1.0 должен был быть описан как bean в конфигурационном файле или классе
 * Начиная с версии 3.1.0 он описан как компонент и не нуждается в дополнительной конфигурации
 */
@Slf4j
@Component
public class RepositoryManagerImpl implements IRepositoryManager {

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IXmlConfigReader xmlConfigReader;

    @Autowired
    @Qualifier("DefaultLoaderXml")
    private ILoaderXml defaultLoaderXml;

    @Getter
    private List<IXmlRepositoryType> repositoryTypes = new ArrayList<>();

    @Autowired
    private void collectRepositoryTypes(List<IXmlRepositoryType> repositoryTypes) {
        this.repositoryTypes = repositoryTypes;
    }

    @Getter
    private List<IRepositoryWrapper> repositories = new ArrayList<>();

    /**
     * В последующих версиях репозитории конфигурируются автоматически на основе найденных XML-файлов
     * @param repositories
     */
    @Deprecated(since = "3.1.0", forRemoval = true)
    public void setRepositories(List<IRepositoryWrapper> repositories) {
        this.repositories = repositories;
    }

    @PostConstruct
    private void init() {
        for (val repositoryType : repositoryTypes) {
            val namespace = repositoryType.getNamespace();
            if (Objects.nonNull(namespace)) {
                val resources = xmlConfigReader.getResourcesByNamespace(XmlConfigScope.LOCAL, namespace);
                for (val resource : resources) {
                    val repositoryWrapper = defaultLoaderXml.load(resource, repositoryType);
                    if (Objects.nonNull(repositoryWrapper)) {
                        repositories.add(repositoryWrapper);
                    }
                }
            }
        }
        verify();
    }

    @Getter
    private List<IRepositoryVerifier> verifiers = new ArrayList<IRepositoryVerifier>();

    @Autowired
    private void collectVerfiers(List<IRepositoryVerifier> verifiers) {
        this.verifiers = verifiers;
    }

    @Override
    public <ELEMENT extends IRepositoryElement> List<ELEMENT> pickAllElements() {
        List<ELEMENT> elements = new ArrayList<>();
        for (IRepositoryWrapper repository  : repositories) {
            elements.addAll(repository.pickAllElements());
        }
        return elements;
    }

    @Override
    public <ELEMENT extends IRepositoryElement> ELEMENT pickElement(String name, Class<ELEMENT> elementType) {
        ELEMENT element = null;
        for (IRepositoryWrapper repository : repositories) {
            element = repository.pickElement(name, elementType);
            if (null != element) {
                break;
            }
        }
        assertNotNull(element, localeManager.getMessage("elementNotFoundInRepositoriesErrorMessage", name));
        return element;
    }

    @Override
    public void verify() {
        List<IRepositoryElement> elements = pickAllElements();
        for (IRepositoryVerifier verifier : verifiers) {
            verifier.verify(elements);
        }
    }
}
