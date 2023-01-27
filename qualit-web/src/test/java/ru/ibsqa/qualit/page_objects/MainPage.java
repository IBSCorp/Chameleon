package ru.ibsqa.qualit.page_objects;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.definitions.annotations.selenium.Page;
import ru.ibsqa.qualit.elements.web.Link;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;

@Component
@Page(name = "Главная страница")
public class MainPage extends DefaultPageObject {

    @Override
    public boolean isLoaded() {
        return homeLink.isDisplayed();
    }

    public MenuBlock menu;

    @Field(name = "QualIT")
    public Link homeLink;

}
