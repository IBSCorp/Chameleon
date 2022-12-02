package ru.ibsqa.qualit.sap.search_context;

import ru.ibsqa.qualit.sap.driver.SapDriver;
import ru.ibsqa.qualit.sap.driver.SapSearchBy;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class SapTableRowSearchContext implements SapSearchContext {

    private final static SapSearchBy searchBy = SpringUtils.getBean(SapSearchBy.class);
    private final SapDriver driver;
    private final ActiveXComponent row;
    private final Variant variant;

    public SapTableRowSearchContext(SapDriver driver, ActiveXComponent row, Variant variant) {
        this.driver = driver;
        this.row = row;
        this.variant = variant;
    }

    public ActiveXComponent getRow() {
        return row;
    }

    public Variant getVariant() {
        return variant;
    }

    public SapDriver getSapDriver() {
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
