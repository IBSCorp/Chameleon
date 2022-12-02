package ru.ibsqa.qualit.page_factory.decorator;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

public abstract class AbstractFieldDecorator implements FieldDecorator {

    @Getter @Setter
    private ElementLocatorFactory elementLocatorFactory;

}
