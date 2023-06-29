package ru.ibsqa.chameleon.uia.search_context;

import ru.ibsqa.chameleon.uia.driver.UiaDriver;
import ru.ibsqa.chameleon.uia.driver.UiaSearchBy;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import mmarquee.automation.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class UiaTableRowSearchContext implements UiaSearchContext {

    private final static UiaSearchBy searchBy = SpringUtils.getBean(UiaSearchBy.class);
    private final UiaDriver driver;
    private final Element row;

    public UiaTableRowSearchContext(UiaDriver driver, Element row) {
        this.driver = driver;
        this.row = row;
    }

    public Element getRow() {
        return row;
    }

    public UiaDriver getUiaDriver() {
        return driver;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return Arrays.asList(findElement(by));
    }

    @Override
    public WebElement findElement(By by) {
        return searchBy.create(this, by);
    }

}
