package ru.ibsqa.chameleon.checker.properties;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.checker.IStartChecker;
import ru.ibsqa.chameleon.definitions.repository.Namespace;
import ru.ibsqa.chameleon.reporter.Environment;
import ru.ibsqa.chameleon.reporter.IReporterManager;
import ru.ibsqa.chameleon.reporter.Parameter;
import ru.ibsqa.chameleon.utils.error.ErrorParser;
import ru.ibsqa.chameleon.utils.xml.IXmlConfigReader;
import ru.ibsqa.chameleon.utils.xml.XmlConfigScope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class SystemPropertiesChecker implements IStartChecker {

    private final static String PROPERTIES_NAMESPACE = Namespace.PREFIX+"properties";

    @Autowired
    private IReporterManager reporterManager;

    @Autowired
    private IXmlConfigReader xmlConfigReader;

    @Override
    public void check() {
        // Все полученные параметры
        Properties systemProperties = System.getProperties();

        // Найти описания свойств
        List<AbstractMetaProperty> defProperties = xmlConfigReader.getResourcesByNamespace(XmlConfigScope.ALL, PROPERTIES_NAMESPACE)
                .stream()
                .flatMap(resource -> loadPropertyDef(resource).stream())
                .collect(Collectors.groupingBy(AbstractMetaProperty::getName))
                .entrySet()
                .stream()
                .map(this::propertyFromGroup)
                //.filter(property -> !property.getType().equals(PropertyType.Hidden))
                .collect(Collectors.toList());

        ValidateResult validateResult = new ValidateResult();
        for (val e : defProperties) {
            if (e instanceof AbstractMetaVisibleProperty) {
                val visibleProperty = (AbstractMetaVisibleProperty)e;
                if (StringUtils.isEmpty(systemProperties.getProperty(e.getName()))
                        && StringUtils.isNoneEmpty(e.getDefaultValue())) {
                    systemProperties.put(visibleProperty.getName(), visibleProperty.getDefaultValue());
                }
                visibleProperty.validate(validateResult, systemProperties.getProperty(e.getName()));
            } else {
                val hiddenProperty = (MetaHidden)e;
                if (StringUtils.isNoneEmpty(hiddenProperty.getDefaultValue())) {
                    systemProperties.put(hiddenProperty.getName(), hiddenProperty.getDefaultValue());
                }
            }
        }
        if (!validateResult.isValid()) {
            fail(validateResult.getMessage());
        }

        Environment environment = new Environment();
        for (AbstractMetaProperty item : defProperties) {
            if (item instanceof AbstractMetaVisibleProperty) {
                val visibleProperty = (AbstractMetaVisibleProperty)item;
                if (visibleProperty.isEnvironment()) {
                    environment.getParameters().add(
                            Parameter
                                    .builder()
                                    .key(visibleProperty.getLabel())
                                    .value(systemProperties.getProperty(visibleProperty.getName()))
                                    .build()
                    );
                }
            }
        }

        // Вывести в отчет значения параметров
        reporterManager.writeEnvironment(environment);
    }

    private AbstractMetaProperty propertyFromGroup(Map.Entry<String, List<AbstractMetaProperty>> group) {
        List<AbstractMetaProperty> variants = group.getValue();
        variants.sort(Comparator.comparing(AbstractMetaProperty::getPriority));
        AbstractMetaProperty property = group.getValue().get(0);
        if (property instanceof MetaList && variants.size()>1) {
            MetaList list = (MetaList)property;
            for (int i=1; i<variants.size(); i++) {
                AbstractMetaProperty variant = variants.get(i);
                if (variant instanceof MetaList) {
                    MetaList otherList = (MetaList)variant;
                    if (Objects.nonNull(otherList.getItems())) {
                        list.getItems().addAll(otherList.getItems());
                        otherList.getItems().clear();
                    }
                }
            }
        }
        return property;
    }

    private List<AbstractMetaProperty> loadPropertyDef(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            Unmarshaller unmarshaller = JAXBContext.newInstance(
                    MetaProperties.class,
                    MetaText.class,
                    MetaList.class,
                    MetaCheckBox.class,
                    MetaHidden.class,
                    MetaItem.class
            ).createUnmarshaller();
            MetaProperties metaProperties = (MetaProperties) unmarshaller.unmarshal(is);
            return metaProperties.getProperties();
        } catch (JAXBException | IOException e) {
            log.warn(String.format("Файл %s не содержит описания настроек или неверный формат файла. Ошибка: %s",
                    resource.getDescription(),
                    ErrorParser.getErrorMessage(e)
            ));
            return Collections.emptyList();
        }
    }
}
