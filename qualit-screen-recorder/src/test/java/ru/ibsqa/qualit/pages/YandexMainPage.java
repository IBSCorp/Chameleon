package ru.ibsqa.qualit.pages;

import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.elements.web.TextInput;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;
import org.springframework.stereotype.Component;

@Component
public class YandexMainPage extends DefaultPageObject {

    @Field(names = "Поиск", locator = "id=text")
    private TextInput search;

    @Override
    public boolean isLoaded() {
        return search.exists();
    }
}
