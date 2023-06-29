package ru.ibsqa.chameleon.utils.spring;

import com.google.common.collect.Streams;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.ibsqa.chameleon.utils.xml.IXmlConfigReader;
import ru.ibsqa.chameleon.utils.xml.XmlConfigScope;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Поставщик списка пакетов для сканирования классов.
 * Обнаруживает component-scan в XML-конфигурациях и @ComponentScan в бинах
 * Фильтры не поддерживаются
 */
@Component
public class BasePackagesResolverImpl implements IBasePackagesResolver {

    private final static String BEANS_ROOT = "beans";
    private final static String COMPONENT_SCAN_TAG = "component-scan";
    private final static String BASE_PACKAGE_ATTR = "base-package";

    @Autowired
    private IXmlConfigReader xmlConfigReader;

    private List<String> basePackages;

    public List<String> getBasePackages() {
        if (Objects.isNull(basePackages)) {
            basePackages =
                    Streams.concat(
                        // Получить basePackages из XML
                        xmlConfigReader
                                .getResourcesByRootTag(XmlConfigScope.LOCAL, BEANS_ROOT)
                                .stream()
                                .map(resource -> findNodeBySubTag(xmlConfigReader.getDocument(resource).getDocumentElement(), COMPONENT_SCAN_TAG))
                                .filter(Objects::nonNull)
                                .map(node -> node.getAttributes().getNamedItem(BASE_PACKAGE_ATTR))
                                .map(Node::getNodeValue)
                                .flatMap(value -> Arrays.stream(value.split(","))),
                        // Получить basePackages из анотаций @ComponentScan
                        SpringUtils.getContext()
                                .getBeansWithAnnotation(ComponentScan.class)
                                .entrySet()
                                .stream()
                                .flatMap((entry) -> AnnotatedElementUtils
                                        .getMergedRepeatableAnnotations(entry.getValue().getClass(), ComponentScan.class)
                                        .stream()
                                        .map(scan -> Pair.of(entry.getValue().getClass(), scan)))
                                .flatMap(pair ->
                                        (pair.getRight().basePackages().length == 0 && pair.getRight().basePackageClasses().length == 0)
                                                ? Stream.of(pair.getLeft().getPackageName())
                                                : Streams.concat(
                                                    Arrays.stream(pair.getRight().basePackages()),
                                                    Arrays.stream(pair.getRight().basePackageClasses()).map(Class::getPackageName)
                                                )
                                )
                    ).distinct()
                    .collect(Collectors.toList());
        }
        return basePackages;
    }

    private Node findNodeBySubTag(Element element, String tagName) {
        Node child = element.getFirstChild();
        while (Objects.nonNull(child)) {
            if (child.getNodeName().equals(tagName) || child.getNodeName().endsWith(":"+tagName)) {
                return child;
            }
            child = child.getNextSibling();
        }
        return null;
    }
}
