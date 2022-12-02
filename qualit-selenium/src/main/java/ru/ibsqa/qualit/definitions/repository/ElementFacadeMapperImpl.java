package ru.ibsqa.qualit.definitions.repository;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaElement;
import ru.ibsqa.qualit.elements.IFacadeMappedByMeta;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.utils.spring.IClassPathScanner;

import java.util.*;
import java.util.stream.Collectors;

/***
 * Стандартная имплементация маппера элементов, загруженных в репозиторий, на
 * реализации этих элементов (ElementFacade)
 * Начиная с версии 3.1 сопоставление делается на основе аннотации @MetaElement поставленной у реализации элемента
 * Сопоставление выполняется с учетом типа драйвера
 * Если подходящий метакласс не найден, то возникает ошибка
 */
@Component
@Slf4j
public class ElementFacadeMapperImpl implements IElementFacadeMapper {

    @Autowired
    private IClassPathScanner classPathScanner;

    @Autowired
    private ILocaleManager localeManager;

    @Override
    public String getFacadeClassName(Class<? extends IMetaElement> metaClass, Class<? extends ISupportedDriver> supportedDriver) {
        if (Objects.isNull(metaClass)) {
            throw new RuntimeException("metaClass is null in getFacadeClassName()");
        }

        // Вспомогательный сценарий для удобного использования в проектах (в библиотеке не применяется)
        // Сопоставление на основе @ElementFacade со стороны метакласса элемента
        ElementFacade elementFacade = (ElementFacade) metaClass.getAnnotation(ElementFacade.class);
        if (Objects.nonNull(elementFacade)) {
            return elementFacade.value().getName();
        }

        // Основной сценарий
        // Сопоставление на основе @MetaElement со стороны фасадного класса элемента (множественное соответствие с учетом класса драйвера и приоритета)
        val candidates = findElementMapping(metaClass).stream()
                .filter(mapping -> mapping.supportedDriver == ISupportedDriver.class ||
                        (Objects.nonNull(supportedDriver) && mapping.supportedDriver.isAssignableFrom(supportedDriver))
                )
                .collect(Collectors.toList());

        if (candidates.size() == 0) {
            throw new RuntimeException(localeManager.getMessage("facadeNotFound", metaClass.getCanonicalName()));
        } else {
            log.debug(String.format("ElementFacadeMapping %s => %s", metaClass.getCanonicalName(), candidates.get(0).facadeClass.getCanonicalName()));
            return candidates.get(0).facadeClass.getCanonicalName();
        }
    }

    private Map<Class<? extends IMetaElement>, List<Mapping>> cache = null;

    private void createCache() {
        // Получить всех кандидатов
        cache = classPathScanner
                .findCandidates(IFacadeMappedByMeta.class)
                .flatMap(elementClass ->
                        Optional.ofNullable(elementClass.getAnnotation(MetaElement.class))
                                .stream()
                                .map(annotation -> (MetaElement)annotation)
                                .map(annotation -> new Mapping(annotation.value(), annotation.supportedDriver(), elementClass, annotation.priority()))
                )
                .collect(Collectors.groupingBy(m -> m.metaClass));

        Comparator<Mapping> compareByPriority = Comparator.comparing(m -> m.priority);
        Comparator<Mapping> compareBySupportedDriver = (o1,o2) -> {
            if (o1.supportedDriver.equals(o2.supportedDriver)) {
                return 0;
            }
            if (o2.supportedDriver.equals(ISupportedDriver.class)) {
                return -1;
            }
            if (o1.supportedDriver.equals(ISupportedDriver.class)) {
                return 1;
            }
            return 0;
        };

        // Отсортировать кандидатов
        cache.forEach((key, value) -> value.sort(compareByPriority.thenComparing(compareBySupportedDriver)));

        // Вывод в лог
        log.debug("ElementFacade map: "+cache.toString());
    }

    private synchronized List<Mapping> findElementMapping(Class<? extends IMetaElement> metaClass) {
        if (Objects.isNull(cache)) {
            createCache();
        }

        return Optional.ofNullable(cache.get(metaClass)).orElseGet(Collections::emptyList);
    }

    @AllArgsConstructor
    @ToString
    private static class Mapping {
        Class<? extends IMetaElement> metaClass;
        Class<? extends ISupportedDriver> supportedDriver;
        Class<? extends IFacadeMappedByMeta> facadeClass;
        ConfigurationPriority priority;
    }

}
