package ru.ibsqa.chameleon.page_objects;

import ru.ibsqa.chameleon.definitions.annotations.selenium.Field;
import ru.ibsqa.chameleon.elements.web.Link;
import ru.ibsqa.chameleon.page_factory.pages.DefaultPageObject;

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
