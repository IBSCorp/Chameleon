package ru.ibsqa.chameleon.uia.search_context;

import ru.ibsqa.chameleon.uia.driver.UiaDriver;
import org.openqa.selenium.SearchContext;

public interface UiaSearchContext extends SearchContext {
    UiaDriver getUiaDriver();
}
