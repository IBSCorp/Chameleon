package ru.ibsqa.qualit.sap.search_context;

import ru.ibsqa.qualit.sap.driver.SapDriver;
import org.openqa.selenium.SearchContext;

public interface SapSearchContext extends SearchContext {
    SapDriver getSapDriver();
}
