package ru.ibsqa.qualit.uia.search_context;

import ru.ibsqa.qualit.uia.driver.UiaDriver;
import org.openqa.selenium.SearchContext;

public interface UiaSearchContext extends SearchContext {
    UiaDriver getUiaDriver();
}
