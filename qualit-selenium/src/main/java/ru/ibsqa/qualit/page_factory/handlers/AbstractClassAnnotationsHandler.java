package ru.ibsqa.qualit.page_factory.handlers;

import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

public abstract class AbstractClassAnnotationsHandler<T extends WebElementFacade> extends AbstractAnnotations {

    @Getter(AccessLevel.PROTECTED)
    private final Class<T> elementClass;

    @Getter(AccessLevel.PROTECTED)
    private final String driverId;

    public AbstractClassAnnotationsHandler(String driverId, Class<T> elementClass) {
        this.driverId = driverId;
        this.elementClass = elementClass;
    }
}

