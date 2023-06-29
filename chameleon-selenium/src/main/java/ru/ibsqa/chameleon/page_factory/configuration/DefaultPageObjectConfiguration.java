package ru.ibsqa.chameleon.page_factory.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class DefaultPageObjectConfiguration implements IPageObjectConfiguration {

    @Getter @Setter
    private String pagesPackage;
}
