package ru.ibsqa.qualit.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.definitions.annotations.selenium.Page;
import ru.ibsqa.qualit.elements.collections.DefaultCollection;
import ru.ibsqa.qualit.elements.web.Button;
import ru.ibsqa.qualit.elements.web.StaticText;
import ru.ibsqa.qualit.page_factory.pages.DefaultCollectionObject;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;

import java.util.List;

@Component
@Page(name = "Список товаров")
public class ListPage extends DefaultPageObject {

    @Override
    public boolean isLoaded() {
        return title.isDisplayed();
    }

    public MenuBlock menu;

    @Field(name = "Заголовок", locator = "//h5[text()=\"Список товаров\"]")
    public StaticText title;

    @Field(name = "Таблица", locator = ".//table/tbody/tr")
    public DefaultCollection<GoodsItem> items;


    public static class GoodsItem extends DefaultCollectionObject {

        @Field(name = "#", locator = ".//th")
        public StaticText rownum;

        @Field(name = "Наименование", locator = ".//td[1]")
        public StaticText name;

        @Field(name = "Тип", locator = ".//td[2]")
        public StaticText type;

        @Field(name = "Экзотический", locator = ".//td[3]")
        public StaticText exotic;

    }

    @Field(name = "Добавить")
    public Button addBtn;

}
