package ru.ibsqa.chameleon.sap.search_context;

import ru.ibsqa.chameleon.sap.driver.SapDriver;
import org.openqa.selenium.SearchContext;

public interface SapSearchContext extends SearchContext {
    SapDriver getSapDriver();
}
