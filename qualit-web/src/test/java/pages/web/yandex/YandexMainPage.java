package pages.web.yandex;

import org.openqa.selenium.SearchContext;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.definitions.annotations.selenium.Page;
import ru.ibsqa.qualit.elements.collections.DefaultCollection;
import ru.ibsqa.qualit.elements.web.StaticText;
import ru.ibsqa.qualit.elements.web.TextInput;
import ru.ibsqa.qualit.page_factory.pages.DefaultCollectionObject;
import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;

@Component
@Page(name = "Стартовая страница Яндекс", locator = "//html")
public class YandexMainPage extends DefaultPageObject {

    public YandexBlockPage yandexBlockPage;

    @Override
    public boolean isLoaded() {
        return searchInput.isDisplayed();
    }

    @Field(names = "Поиск", locator = "//*[@aria-label='Запрос']")
    public TextInput searchInput;

    /*
    @Field(names = "Поиск", locator = "//*[@aria-label='Запрос']")
    public List<TextInput> searchInputs;
    */

    @Field(names = "Сейчас в СМИ", locator = "//div[@id='news_panel_news']/div/ol[not(contains(@class,'animation'))]/li")
    public DefaultCollection<NewsItem> news;

    public static class NewsItem extends DefaultCollectionObject {
        public NewsItem(SearchContext searchContext){
            super(searchContext);
        }
        @Field(names = "Текст новости", locator = "./a")
        public StaticText newsText;

    }

}
