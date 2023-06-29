package ru.ibsqa.chameleon.definitions.repository.csvloader;

import ru.ibsqa.chameleon.definitions.repository.ILoader;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryWrapper;

/**
 * Загрузчик из CSV, нужен для совместимости с существующими описаниями элементов.
 * Возможно надо несколько имплементаций, поскольку на разных проектах структура файла может немного отличаться.
 * Поддерживает загрузку только элементов selenium.
 */
public class LoaderCsvImpl implements ILoader {

    @Override
    public boolean load(IRepositoryWrapper repositoryWrapper, String fileName) {
        //TODO
        return false;
    }

}
