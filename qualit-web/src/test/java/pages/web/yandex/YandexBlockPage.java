package pages.web.yandex;

import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.elements.web.Button;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;

public class YandexBlockPage extends DefaultPageObject {

    @Field(names = "Найти", locator = "//button[.//text()='Найти']")
    public Button searchBtn;

    @Override
    public boolean isLoaded() {
        return true;
    }
}
