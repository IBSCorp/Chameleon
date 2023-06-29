package ru.ibsqa.chameleon.sap.elements;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaCollection;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.elements.collections.AbstractCollection;
import ru.ibsqa.chameleon.elements.collections.AbstractCollectionIterator;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import ru.ibsqa.chameleon.sap.driver.SapDriver;
import ru.ibsqa.chameleon.sap.driver.SapSearchBy;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
@MetaElement(value = MetaCollection.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapCollection<PAGE extends IPageObject> extends AbstractCollection<PAGE> {

    private ActiveXComponent component;

    @Override
    public void pushArguments(final String collectionName, final int waitTimeOut, final ElementLocator elementLocator, final String[] frames, final Class<PAGE> collectionObjectClass) {
        super.pushArguments(collectionName, waitTimeOut, elementLocator, frames, collectionObjectClass);
        SapDriver driver = (SapDriver) SpringUtils.getBean(IDriverManager.class).getLastDriver().getWrappedDriver();
        SapSearchBy searchBy = SpringUtils.getBean(SapSearchBy.class);
        this.component = searchBy.create(driver, this.getElementLocator());
    }

    @Override
    public Iterator<PAGE> iterator() {
        return new SapCollectionIterator(getCollectionName(), getWaitTimeOut(),getElementLocator(), getFrames(), getCollectionObjectClass());
    }

    public ActiveXComponent getComponent(){
        SapSearchBy searchBy = SpringUtils.getBean(SapSearchBy.class);
        SapDriver driver = (SapDriver) SpringUtils.getBean(IDriverManager.class).getLastDriver().getWrappedDriver();
        return searchBy.create(driver, this.getElementLocator());
    }

    public static class SapCollectionIterator<PAGE extends IPageObject> extends AbstractCollectionIterator<PAGE> {
        private SapDriver driver = null;
        private Iterator<Variant> rowIterator = null;

        public SapCollectionIterator(String collectionName, int waitTimeOut, ElementLocator elementLocator, String[] frames, Class<PAGE> collectionObjectClass) {
            super(collectionName, waitTimeOut, elementLocator, frames, collectionObjectClass);
            rowIterator = getRows().iterator();
        }

//        public SapCollectionIterator(String collectionName, ElementLocator elementLocator, String[] frames, Class<PAGE> collectionObjectClass) {
//            super(collectionName, elementLocator, frames, collectionObjectClass);
//            rowIterator = getRows().iterator();
//        }

        private List<Variant> getRows() {
            List<Variant> rows =  new ArrayList<>();

            if (this.getElementLocator() != null) {

                SapSearchBy searchBy = SpringUtils.getBean(SapSearchBy.class);
                this.driver = (SapDriver) SpringUtils.getBean(IDriverManager.class).getLastDriver().getWrappedDriver(); // TODO обязательно переделать это, контекст надо брать от elementLocator
                ActiveXComponent table = searchBy.create(driver, this.getElementLocator());

                try {
                    int visibleCount = Integer.parseInt(table.getProperty("VisibleRowCount").toString());
                    for(int i = 0; i < visibleCount; i++){
                            ActiveXComponent currentRows = new ActiveXComponent(table.getProperty("Rows").toDispatch());
                            rows.add(currentRows.invoke("item", i));
                    }
                } catch (Exception e) {
                    log.error(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString(), e);
             //       Assert.fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
                }
            }
            return rows;
        }



        @Override
        protected WebElement getNextWebElement() {
            return new SapElement(SapElementType.TABLE_ROW, current, driver);
        }

        private Variant current = null;

        @Override
        public boolean hasNext() {
            while (rowIterator.hasNext()) {
                current = rowIterator.next();
                return true;
            }
            return false;
        }
    }
}
