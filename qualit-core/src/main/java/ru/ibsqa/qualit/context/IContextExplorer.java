package ru.ibsqa.qualit.context;

import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.storage.IVariableScope;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Эту тушку шаги будут спрашивать, найди мне поле во всех текущих контекстах
 * Он будет опрашивать на предмет наличия поля все существующие менеджеры контекстов, пока кто-то из них не откликнется, что поле есть
 * В любом случае менеджеры должны вернуть список своих актуальных контекстов, чтобы шаг мог в сообщение об ошибке добавить,
 * где искалось поле (на какой странице или страницах) и не было найдено
 *
 * возвращается некое совсем пустой интерфейс поля, который ничего не может IField (существующий IField надо переименовать в ISeleniumField)
 * сверху будут существовать следующие интерфейсы:
 * поле, которое можно прочитать IReadableField
 * поле, в которое можно записать IWritableField
 * оба варианта
 * селениумовское поле ISeleniumField, с которым можно попытаться сделать еще что-то дополнительное, например кликнуть.
 * Но например кнопки я бы не стал снабжать интерфейсом IWritableField
 */
public interface IContextExplorer extends ISearchElementException {

    default <FACADE extends IFacade, CONTEXT extends IContextObject> PickElementResult<FACADE, CONTEXT> pickElement(String fullPathName, Class<FACADE> facadeClass) {
        try {
            return searchElement(fullPathName, facadeClass);
        } catch (SearchElementException e) {
            fail(e.getMessage());
        }
        return null;
    }

    <FACADE extends IFacade, CONTEXT extends IContextObject> PickElementResult<FACADE, CONTEXT> searchElement(String fullPathName, Class<FACADE> facadeClass) throws SearchElementException;

    void addContextManager(IContextManager contextManager);

    void removeContextManager(IContextManager contextManager);

    IVariableScope getPickElementScope();

    IVariableScope resetPickElementScope();

}
