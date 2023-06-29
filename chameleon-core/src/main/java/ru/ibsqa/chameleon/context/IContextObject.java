package ru.ibsqa.chameleon.context;

import ru.ibsqa.chameleon.elements.IFacade;

/**
 * В терминах selenium - это IPageObject, он и должен имплементировать этот интерфейс
 * Этот интерфейс должен уметь найти реальное поле через драйвер (в случае Selenium это WebElement)
 * для поиска использует информацию из репозитория
 *
 * REST
 * ищет поле в запросах и ответах, и предоставляет возможность читать/писать данные
 */
public interface IContextObject {

    <T extends IFacade> T getField(String fieldName);

}
