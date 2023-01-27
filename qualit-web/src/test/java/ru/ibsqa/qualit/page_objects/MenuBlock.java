package ru.ibsqa.qualit.page_objects;

import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.elements.web.Link;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;

public class MenuBlock extends DefaultPageObject {

    @Field(name = "Песочница")
    public Link sandbox;

    @Field(name = "Товары")
    public Link goods;

    @Field(name = "Тест Iframe")
    public Link iframe;

    @Field(name = "Сброс данных")
    public Link reset;

    @Override
    public boolean isLoaded() {
        return true;
    }

}
