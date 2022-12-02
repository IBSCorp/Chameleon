package ru.ibsqa.qualit.utils.xml;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class XmlConfigReaderImpl implements IXmlConfigReader {

    @Autowired
    private List<IXmlResourceFilter> xmlResourceFilters;

    private List<XmlResource> resourcesCache = null;

    private final String LOCATION_PATTERN = "classpath*:/**/*.xml";

    @Override
    public Document getDocument(Resource resource) {
        try (InputStreamReader isr = new InputStreamReader(resource.getInputStream())) {
            InputSource is = new InputSource();
            is.setCharacterStream(isr);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setNamespaceAware(true);
            try {
                dbf.setFeature("http://xml.org/sax/features/namespaces", false);
                dbf.setFeature("http://xml.org/sax/features/validation", false);
                dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
                dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            } catch (ParserConfigurationException ignore) {}
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(is);
            return doc;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.debug(String.format("Ошибка при анализе ресурса %s: %s", resource.getDescription(), e.getMessage()), e);
            return null;
        }
    }

    public String getRootElement(Resource resource) {
        return Optional
                .ofNullable(getDocument(resource))
                .map(Document::getDocumentElement)
                .map(Node::getNodeName)
                .orElse(null);
    }

    public String getNamespace(Resource resource) {
        return Optional
                .ofNullable(getDocument(resource))
                .map(Document::getDocumentElement)
                .map(node -> node.getAttribute("xmlns"))
                .orElse(null);
    }

    /**
     * Найти все файлы, содержащие настройки в виде XML
     * @param scope - расположение ресурса
     * @param namespace - неймспейс схемы
     * @return
     */
    @Override
    public List<Resource> getResourcesByNamespace(XmlConfigScope scope, String namespace) {
        return getResourcesCache()
                .stream()
                .filter(r -> scope.equals(XmlConfigScope.ALL) || r.getResource().isFile())
                .filter(r -> r.getNamespace().equals(namespace))
                .filter(r -> xmlResourceFilters.stream().allMatch(f -> f.apply(r)))
                .map(XmlResource::getResource)
                .collect(Collectors.toList());
    }

    /**
     * Найти все файлы, содержащие настройки в виде XML
     * @param scope - расположение ресурса
     * @param root - корневой тег
     * @return
     */
    @Override
    public List<Resource> getResourcesByRootTag(XmlConfigScope scope, String root) {
        return getResourcesCache()
                .stream()
                .filter(r -> scope.equals(XmlConfigScope.ALL) || r.getResource().isFile())
                .filter(r -> r.getRoot().equals(root))
                .map(XmlResource::getResource)
                .collect(Collectors.toList());
    }

    private List<XmlResource> getResourcesCache() {
        if (Objects.isNull(resourcesCache)) {

            long start = System.currentTimeMillis();

            Resource[] resources = SpringUtils.getResources(LOCATION_PATTERN);

            if (Objects.isNull(resources)) {
                resourcesCache = Collections.emptyList();
            } else {
                resourcesCache = Arrays.stream(resources)
                        .filter(r ->
                                Objects.nonNull(r.getFilename())
                                        && !r.getFilename().equalsIgnoreCase("pom.xml")
                                        && !r.getDescription().contains("/META-INF/")
                        ).map(r -> XmlResource.builder()
                                .namespace(getNamespace(r))
                                .root(getRootElement(r))
                                .resource(r)
                                .build()
                        ).filter(r -> !Objects.isNull(r.getNamespace()))
                        .collect(Collectors.toList());
            }

            log.info(String.format("Поиск XML-ресурсов %d ms, найдено: %d", System.currentTimeMillis() - start, resourcesCache.size()));

            for (XmlResource xmlResource : resourcesCache) {
                log.debug(String.format("Найден ресурс %s %s", xmlResource.getNamespace(), xmlResource.getResource().getDescription()));
            }
        }

        return resourcesCache;
    }
}
