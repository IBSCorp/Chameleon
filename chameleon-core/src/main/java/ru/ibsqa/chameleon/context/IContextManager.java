package ru.ibsqa.chameleon.context;

import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.i18n.ILocaleManager;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Эта тушка будет помнить текущий контекст (текущую страницу в терминах selenium)
 * Во все такие менеджеры перенаправляется запрос от IContextExplorer, пока кто-то из них не скажет, что поле найдено
 *
 * SELENIUM
 * Продвинутые имплементации, типа селениумовской, должны будут помнить стек контекстов с предоставлением навигации вперед-назад
 * если идет работа с двумя приложениями одновременно, то наверное нужно создавать дополнительный контекст-менеджер,
 * который будет хранить текущую страницу второго приложения, но как при этом использовать шаги, не понятно, надо подумать
 *
 * REST:
 * контекст ContextManagerResponseImpl должен помнить все ответы (последний отдельно)
 * Для обращения к полям предпоследнего и более ранних ответов на запросы надо использовать формат [Имя_ответа:]Имя_поля
 * Если имя не указано, то поле ищется только в последнем ответе
 * Имена ответам будут присваивать шаги, выполняющие запросы, но не обязательно (When сохранить ответ с именем ${answerName})
 * ContextManagerRequestImpl всегда указывает на формируемый запрос
 */
public interface IContextManager<CONTEXT extends IContextObject> {

    /**
     * Без имплементации менеджер не поддерживает поиск элементов внутри себя
     * @param fullPathName
     * @param fieldType
     * @param <FACADE>
     * @return
     */
    default <FACADE extends IFacade> PickElementResult<FACADE,CONTEXT> pickElement(String fullPathName, Class<FACADE> fieldType) {
        return null;
    }

    /**
     * По умолчанию именем контекста является название его класса.
     * Название контекста используется для вывода сообщений
     * @return
     */
    default String getContextName() {
        return this.getClass().getSimpleName();
    }

    /**
     * По умолчанию кодом контекста является его название
     * Код контекста используется для указания в префиксе поля, например:
     * Коллекция::Сумма услуги
     * @return
     */
    default String getContextCode() {
        return this.getContextName();
    }

    /**
        На текущий момент функционал организован так, что извне контекст требуется устанавливать только некоторым шагам работы с коллекцией.
        В общем случае менеджер контекста может помнить сразу несколько IContextObject и он должен самостоятельно решать, как реагировать на
        вызов данного метода.
     */
    default void setContextCollectionItem(IContextObject contextObject) {
        fail(ILocaleManager.message("collectionNotImplementedErrorMessage"));
    }

}
