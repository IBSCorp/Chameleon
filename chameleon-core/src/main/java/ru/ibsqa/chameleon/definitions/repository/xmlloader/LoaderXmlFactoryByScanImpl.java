package ru.ibsqa.chameleon.definitions.repository.xmlloader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.utils.spring.IClassPathScanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LoaderXmlFactoryByScanImpl implements ILoaderXmlFactoryByScan {

    @Autowired
    //private IBasePackagesResolver basePackagesResolver;
    private IClassPathScanner classPathScanner;

    @Override
    public Unmarshaller getUnmarshaller(IXmlRepositoryType repositoryType) {

        // Сканировать все классы в поисках имплементаций метаинтерфейса, заданного в параметре repositoryType
        List<Class<?>> list = classPathScanner.findCandidates(repositoryType.getAssignableFrom())
                        // Приоритезация
                        .map(c-> Pair.of(c.getAnnotation(XmlRootElement.class), c))
                        .filter(p -> Objects.nonNull(p.getLeft()))
                        .collect(Collectors.groupingBy(p -> p.getLeft().name()))
                        .values()
                        .stream()
                        .map(m ->
                                m.stream()
                                        .map(Pair::getRight)
                                        .min((Comparator<Class<?>>) (c1, c2) -> getPriority(c1).compareTo(getPriority(c2)))
                                        .orElse(null)
                        )
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(list.toArray(new Class<?>[0]));
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller;
        } catch (JAXBException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private ConfigurationPriority getPriority(Class<?> aClass) {
        return Optional.ofNullable(aClass)
                .map(c -> c.getAnnotation(MetaPriority.class))
                .map(MetaPriority::value)
                .orElse(ConfigurationPriority.NORMAL);
    }

}
