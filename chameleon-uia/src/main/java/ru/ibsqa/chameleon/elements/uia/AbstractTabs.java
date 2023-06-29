package ru.ibsqa.chameleon.elements.uia;

/**
 * Подумать в сторону коллекций. Реализует абстрактную панель с закладками.
 */
public abstract class AbstractTabs extends UiaElementFacade {

    /**
     * Необходимо научиться определять какая текущая закладка выделена, но UiAutomation этого не умеет
     *
     * @return
     */
    @Override
    public String getText() {
        return "";
    }

}
