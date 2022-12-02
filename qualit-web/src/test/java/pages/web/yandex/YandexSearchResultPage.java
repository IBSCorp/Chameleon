package pages.web.yandex;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.definitions.annotations.selenium.Page;
import ru.ibsqa.qualit.elements.web.StaticText;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;

@Component
@Page(name = "Результаты поиска")
public class YandexSearchResultPage extends DefaultPageObject {

    @Field(names = "Результат поиска", locator = "//*[@class='entity-search__header']/*[contains(@class, 'entity-search__title')]")
    public StaticText searchResult;

    @Override
    public boolean isLoaded() {
        return true;
    }
}
