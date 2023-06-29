package ru.ibsqa.chameleon.definitions.repository.selenium;

public interface IMetaElement extends IRepositoryElementSelenium {
    String getName();

    String getLocator();

    String getTemplate();

    String[] getFrames();
}
