package ru.ibsqa.chameleon.utils.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@Slf4j
public class ClassPathScannerImpl implements IClassPathScanner {

    @Autowired
    private IBasePackagesResolver basePackagesResolver;

    @Override
    public <E extends Class<T>, T> Stream<E> findCandidates(Class<T> assignableType) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider( false);
        scanner.addIncludeFilter(new AssignableTypeFilter(assignableType));
        return basePackagesResolver
                .getBasePackages()
                .stream()
                .flatMap(basePackage -> scanner.findCandidateComponents(basePackage).stream())
                .map(beanDefinition -> {
                    try {
                        return (E)Class.forName(beanDefinition.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        log.warn(e.getLocalizedMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }
}
