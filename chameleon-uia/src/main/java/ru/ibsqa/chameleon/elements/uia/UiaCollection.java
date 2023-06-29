package ru.ibsqa.chameleon.elements.uia;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaCollection;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.elements.collections.AbstractCollection;
import ru.ibsqa.chameleon.elements.collections.AbstractCollectionIterator;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.uia.driver.UiaDriver;
import ru.ibsqa.chameleon.uia.driver.UiaSearchBy;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.Element;
import mmarquee.uiautomation.TreeScope;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaCollection.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class UiaCollection<PAGE extends IPageObject> extends AbstractCollection<PAGE> {

    @Override
    public Iterator<PAGE> iterator() {
        return new UiaCollectionIterator(getCollectionName(), getWaitTimeOut(), getElementLocator(), getFrames(), getCollectionObjectClass());
    }

    public static class UiaCollectionIterator<PAGE extends IPageObject> extends AbstractCollectionIterator<PAGE> {
        private UiaDriver driver = null;
        private Iterator<Element> rowIterator = null;

        public UiaCollectionIterator(String collectionName, int waitTimeOut, ElementLocator elementLocator, String[] frames, Class<PAGE> collectionObjectClass) {
            super(collectionName, waitTimeOut, elementLocator, frames, collectionObjectClass);
            rowIterator = getRows().iterator();
        }

        private List<Element> getRows() {
            List<Element> rows = null;

            if (this.getElementLocator() != null) {

                UiaSearchBy searchBy = SpringUtils.getBean(UiaSearchBy.class);
                this.driver = (UiaDriver) SpringUtils.getBean(IDriverManager.class).getLastDriver(); // TODO обязательно переделать это, контекст надо брать от elementLocator
                Element table = searchBy.create(driver, this.getElementLocator()).getElement();

                try {
                    rows =
                            table.findAll(
                                    new TreeScope(TreeScope.CHILDREN),
                                    driver
                                            .getAutomation()
                                            .createTrueCondition()
                            );
                } catch (AutomationException e) {
                    log.error(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString(), e);
                    fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
                }
            }
            return rows;
        }

        private boolean isRow(Element element) {
            try {
                //row 1
                //List`1 row 2
                //select1 row 2
                //DynamicSelect row 1
                // AutomationId = Row 1
                //if (!row.getName().matches("List`(\\d+)\\srow\\s(\\d+)") && !row.getName().matches("select(\\d+)\\srow\\s(\\d+)")) {
                if (!element.getName().matches("(.*)\\s?row\\s(\\d+)") && !element.getAutomationId().matches("(.*)\\s?Row.*(\\d+)")
                        ||
                        element.findAll(new TreeScope(TreeScope.CHILDREN), driver
                                .getAutomation()
                                .createTrueCondition()).size() == 0) {
                    return false;
                }
                return true;
            } catch (AutomationException e) {
                return false;
            }
        }

        @Override
        protected WebElement getNextWebElement() {
            return new UiaElement(UiaElementType.TABLE_ROW, null, current, driver);
        }

        private Element current = null;

        @Override
        public boolean hasNext() {

            Element elem = null;
            while (rowIterator.hasNext()) {

                elem = rowIterator.next();
                if (isRow(elem)) {
                    current = elem;
                    return true;
                }
            }
            return false;
        }
    }
}
