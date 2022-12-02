package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.sap.driver.SapDriver;
import ru.ibsqa.qualit.sap.driver.SapSearchBy;
import ru.ibsqa.qualit.sap.sapEnum.SapKeyEnum;
import ru.ibsqa.qualit.sap.search_context.SapSearchContext;
import ru.ibsqa.qualit.sap.search_context.SapTableRowSearchContext;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;

import java.util.List;
import java.util.Objects;

@Slf4j
public class SapElement extends ActiveXComponent implements ISapElement {

    @Getter
    private final SapElementType sapElementType;
    private WebDriverFacade driver;
    private Variant variant;

    private final SapSearchBy searchBy = SpringUtils.getBean(SapSearchBy.class);

    public SapElement(SapElementType sapElementType, Variant variant, WebDriverFacade driver) {
        super(variant.getDispatch());
        this.sapElementType = Objects.isNull(sapElementType) ? SapElementType.GENERAL : sapElementType;
        this.variant = variant;
        this.driver = driver;
    }

    public SapElement(String programId, SapElementType sapElementType) {
        super(programId);
        this.sapElementType = sapElementType;
    }

    @Override
    public void click() {
        invoke("press");
    }

    @Override
    public void submit() {
        invoke("sendVKey", "00");
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        if (keysToSend.length == 1) {
            setProperty("text", keysToSend[0].toString());
        }
    }

    @Override
    public void clear() {
        setProperty("text", "");
    }

    @Override
    public String getTagName() {
        return null;
    }

    @Override
    public String getAttribute(String name) {
        return getProperty(name).toString();
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return getProperty("Changeable").getBoolean();
    }

    @Override
    public String getText() {
        return getProperty("text").toString().trim();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getWrappedElement().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        String locator = by.toString().replaceAll(".*: ", "");
        log.debug(locator);
        ActiveXComponent activeXComponent = new ActiveXComponent(getVariant().toDispatch());
        if (getSapElementType().equals(SapElementType.TABLE_ROW)) {
            SapSearchContext searchContext = new SapTableRowSearchContext((SapDriver) getDriver(), activeXComponent, getVariant());
            return searchBy.create(searchContext, by);
        }
        return driver.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return true;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(String propertyName) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }

    @Override
    public String getPlaceholder() {
        return null;
    }

    @Override
    public void pressKey(KeyEnum key) {
        String sapKeyEvent = SapKeyEnum.valueOf(key.name()).getValue();
        invoke("sendVKey", sapKeyEvent);
    }

    @Override
    public void doubleClick() {
		throw new UnsupportedOperationException("операция не реализована");
    }

    @Override
    public void type(String value) {
        clear();
        sendKeys(value);
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public WebDriverFacade getDriver() {
        return driver;
    }

    @Override
    public WebElement getWrappedElement() {

        return this;
    }

    @Override
    public Coordinates getCoordinates() {
        return null;
    }

    public Variant getVariant() {
        return variant;
    }
}