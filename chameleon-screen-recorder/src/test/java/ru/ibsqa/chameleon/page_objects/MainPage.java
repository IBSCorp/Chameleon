package ru.ibsqa.chameleon.page_objects;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.annotations.selenium.Field;
import ru.ibsqa.chameleon.definitions.annotations.selenium.Page;
import ru.ibsqa.chameleon.elements.web.Link;
import ru.ibsqa.chameleon.page_factory.pages.DefaultPageObject;

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
