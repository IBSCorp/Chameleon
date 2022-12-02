package impls;

import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.page_factory.locator.DefaultSearchStrategy;

@Component
@Log
public class CustomSearchStrategy extends DefaultSearchStrategy {

    @Override
    public By getLocator(String locator) {
        //log.info("#CUSTOM SEARCH STRATEGY# locator=" + locator);
        return super.getLocator(locator);
    }
}