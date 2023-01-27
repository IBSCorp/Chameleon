package ru.ibsqa.qualit.page_objects;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.definitions.annotations.selenium.Page;
import ru.ibsqa.qualit.elements.web.*;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;

@Component
@Page(name = "Добавление товара")
public class FormPage extends DefaultPageObject {

    @Override
    public boolean isLoaded() {
        return title.isDisplayed();
    }

    @Field(name = "Заголовок", locator = "//h5[text()=\"Добавление товара\"]")
    public StaticText title;

    @Field(name = "Наименование")
    public TextInput name;

    @Field(name = "Тип")
    public ComboBox type;

    @Field(name = "Экзотический")
    public CheckBox exotic;

    @Field(name = "Сохранить")
    public Button saveBtn;

}
