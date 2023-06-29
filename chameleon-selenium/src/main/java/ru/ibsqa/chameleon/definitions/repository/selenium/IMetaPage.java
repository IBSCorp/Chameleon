package ru.ibsqa.chameleon.definitions.repository.selenium;

import java.util.List;

public interface IMetaPage extends IMetaContainer {
    String getDriver();

    String getCustomType();

    List<IMetaBlock> getBlocks();

    List<IMetaPage> getAllChildPages();

    List<IMetaCollection> getCollections();
}
